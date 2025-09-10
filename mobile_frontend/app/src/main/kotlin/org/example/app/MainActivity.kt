package org.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageButton
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import org.example.app.R

/**
 * PUBLIC_INTERFACE
 * MainActivity is the entry point of the Q&A Android application.
 * It renders a dark-themed UI with a top app bar, central input for user questions,
 * and a conversation history list showing both user queries and answers.
 *
 * Parameters: None
 * Returns: Activity lifecycle managed by Android OS.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: QAViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[QAViewModel::class.java]

        // Toolbar
        val toolbar: Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        // RecyclerView for conversation
        val recycler: RecyclerView = findViewById(R.id.conversationRecycler)
        val adapter = ConversationAdapter()
        recycler.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        recycler.adapter = adapter

        // Input and send
        val input: EditText = findViewById(R.id.inputQuestion)
        val send: ImageButton = findViewById(R.id.sendButton)

        fun dispatchSend() {
            val text = input.text?.toString()?.trim().orEmpty()
            if (text.isNotEmpty()) {
                viewModel.ask(text)
                input.setText("")
            }
        }

        send.setOnClickListener { dispatchSend() }
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE) {
                dispatchSend()
                true
            } else {
                false
            }
        }

        // Observe conversation
        viewModel.conversation.observe(this) { list ->
            adapter.submit(list)
            recycler.scrollToPosition(adapter.itemCount - 1)
        }
    }
}
