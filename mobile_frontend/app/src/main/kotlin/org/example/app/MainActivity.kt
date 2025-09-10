package org.example.app

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import org.apache.commons.text.WordUtils
import org.example.app.network.ApiClient
import org.example.app.network.ApiResult
import org.example.app.ui.ConversationAdapter
import org.example.app.ui.ConversationItem
import org.example.app.ui.ConversationRole

/**
 * PUBLIC_INTERFACE
 * MainActivity is the entry point of the Q&A mobile frontend.
 * It provides:
 * - A top app bar with the app title
 * - A centered input field for user questions
 * - A send button
 * - A scrollable conversation area showing Q&A history
 *
 * Networking:
 * Uses ApiClient to send questions via REST. If no backend is configured,
 * a mock response is returned to demonstrate functionality.
 */
class MainActivity : Activity() {

    private lateinit var toolbar: Toolbar
    private lateinit var questionInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyHint: TextView

    private lateinit var adapter: ConversationAdapter
    private val items = mutableListOf<ConversationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        toolbar = findViewById(R.id.topAppBar)
        questionInput = findViewById(R.id.inputQuestion)
        sendButton = findViewById(R.id.buttonSend)
        listView = findViewById(R.id.conversationList)
        progressBar = findViewById(R.id.loadingBar)
        emptyHint = findViewById(R.id.emptyHint)

        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))

        adapter = ConversationAdapter(this, items)
        listView.adapter = adapter

        // Enable send only when there's text
        sendButton.isEnabled = false
        questionInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.isEnabled = !s.isNullOrBlank()
            }
        })

        // Send on IME action
        questionInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND && sendButton.isEnabled) {
                onSendClicked()
                true
            } else {
                false
            }
        }

        // Send on button click
        sendButton.setOnClickListener { onSendClicked() }

        // Show demonstration greeting
        val greeting = WordUtils.capitalize("ask me anything about your data or this demo")
        addAssistantMessage(greeting)
    }

    private fun onSendClicked() {
        val text = questionInput.text?.toString()?.trim().orEmpty()
        if (text.isBlank()) return

        emptyHint.visibility = View.GONE
        addUserMessage(text)
        questionInput.setText("")

        progressBar.visibility = View.VISIBLE
        sendButton.isEnabled = false
        questionInput.isEnabled = false

        // Perform the "network" call on a background thread (simple Thread for no extra deps)
        Thread {
            val api = ApiClient(this)
            val result = api.askQuestion(text, history = items)

            runOnUiThread {
                progressBar.visibility = View.GONE
                sendButton.isEnabled = true
                questionInput.isEnabled = true

                when (result) {
                    is ApiResult.Success -> addAssistantMessage(result.answer)
                    is ApiResult.Error -> addAssistantMessage(getString(R.string.error_prefix) + " " + result.message)
                }
                scrollToBottom()
            }
        }.start()
    }

    private fun addUserMessage(text: String) {
        items.add(ConversationItem(ConversationRole.USER, text))
        adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    private fun addAssistantMessage(text: String) {
        items.add(ConversationItem(ConversationRole.ASSISTANT, text))
        adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    private fun scrollToBottom() {
        listView.post {
            listView.setSelection(adapter.count - 1)
        }
    }
}
