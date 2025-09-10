package org.example.app.ui

/**
 * PUBLIC_INTERFACE
 * ConversationRole identifies whether a message is from the user or assistant.
 */
enum class ConversationRole { USER, ASSISTANT }

/**
 * PUBLIC_INTERFACE
 * ConversationItem represents a single message in the conversation history.
 */
data class ConversationItem(
    val role: ConversationRole,
    val message: String
)
