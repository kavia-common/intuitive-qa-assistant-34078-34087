package org.example.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * QAViewModel manages conversation state and orchestrates calls to the backend API.
 *
 * Exposes:
 * - conversation: LiveData<List<Message>> representing the chat history.
 * - ask(question: String): Sends a question, appends user message, and fetches answer.
 */
class QAViewModel : ViewModel() {

    private val _conversation = MutableLiveData<List<Message>>(emptyList())
    val conversation: LiveData<List<Message>> = _conversation

    private val repository = QARepository()

    // PUBLIC_INTERFACE
    fun ask(question: String) {
        /** Sends the question to the backend and updates the conversation with response. */
        append(Message(role = Role.User, content = question))
        viewModelScope.launch {
            try {
                val answer = repository.ask(question)
                append(Message(role = Role.Assistant, content = answer.ifEmpty { "No response." }))
            } catch (e: Exception) {
                append(Message(role = Role.System, content = "Error: ${e.message ?: "Unknown error"}"))
            }
        }
    }

    private fun append(newMsg: Message) {
        val updated = _conversation.value.orEmpty() + newMsg
        _conversation.value = updated
    }
}
