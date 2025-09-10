package org.example.app.network

import android.content.Context
import android.util.Base64
import org.example.app.ui.ConversationItem
import org.example.app.ui.ConversationRole
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * ApiClient encapsulates REST communication with the Q&A backend.
 *
 * If BACKEND_BASE_URL is not provided via resources or env, it falls back to a mock/stub
 * that generates deterministic answers for demonstration purposes.
 */
class ApiClient(private val context: Context) {

    // Try to read base URL from resources; if absent, use null which triggers mock
    private val baseUrl: String? by lazy {
        val id = context.resources.getIdentifier("backend_base_url", "string", context.packageName)
        if (id != 0) context.getString(id).ifBlank { null } else null
    }

    /**
     * PUBLIC_INTERFACE
     * askQuestion sends the user question and optional history to the backend.
     * Parameters:
     * - question: String user question
     * - history: List of ConversationItem representing previous turns
     * Returns ApiResult with an answer or error.
     */
    fun askQuestion(question: String, history: List<ConversationItem>): ApiResult {
        // If baseUrl is not configured, return a mock response
        val url = baseUrl
        return if (url.isNullOrBlank()) {
            ApiResult.Success(
                answer = mockAnswer(question, history)
            )
        } else {
            performPost("$url/qa/ask", question, history)
        }
    }

    private fun performPost(endpoint: String, question: String, history: List<ConversationItem>): ApiResult {
        return try {
            val url = URL(endpoint)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doInput = true
                doOutput = true
                connectTimeout = 8000
                readTimeout = 15000
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                // Example: If auth is needed in the future, set header here
                // setRequestProperty("Authorization", "Bearer $token")
            }

            val json = buildJson(question, history)
            conn.outputStream.use { os ->
                val bytes = json.toByteArray(StandardCharsets.UTF_8)
                os.write(bytes)
            }

            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val response = BufferedReader(InputStreamReader(stream)).use { it.readText() }
            conn.disconnect()

            if (code in 200..299) {
                // Try to parse very simple JSON {"answer": "..."}
                val answer = extractAnswer(response)
                if (answer != null) {
                    ApiResult.Success(answer)
                } else {
                    ApiResult.Success(response.ifBlank { "No answer content." })
                }
            } else {
                ApiResult.Error("HTTP $code: $response")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message ?: "unknown"}")
        }
    }

    private fun buildJson(question: String, history: List<ConversationItem>): String {
        // Build minimal JSON without external libs
        val safeQ = question.replace("\"", "\\\"")
        val historyJson = history.joinToString(separator = ",") { item ->
            val role = if (item.role == ConversationRole.USER) "user" else "assistant"
            val content = item.message.replace("\"", "\\\"")
            "{\"role\":\"$role\",\"message\":\"$content\"}"
        }
        return "{\"question\":\"$safeQ\",\"history\":[${historyJson}]}"
    }

    private fun extractAnswer(response: String): String? {
        // Minimal parser to find "answer":"..."
        val key = "\"answer\""
        val idx = response.indexOf(key)
        if (idx < 0) return null
        val start = response.indexOf(':', idx)
        if (start < 0) return null
        var i = start + 1
        while (i < response.length && response[i].isWhitespace()) i++
        if (i >= response.length || response[i] != '"') return null
        i++ // after opening quote
        val sb = StringBuilder()
        while (i < response.length && response[i] != '"') {
            val ch = response[i]
            if (ch == '\\' && i + 1 < response.length) {
                val next = response[i + 1]
                sb.append(next)
                i += 2
            } else {
                sb.append(ch)
                i++
            }
        }
        return sb.toString()
    }

    private fun mockAnswer(question: String, history: List<ConversationItem>): String {
        val count = history.count { it.role == ConversationRole.USER }
        val base = "This is a mock answer for: \"$question\""
        val tip = when {
            question.lowercase(Locale.ROOT).contains("hello") -> "Hello! How can I help you today?"
            question.lowercase(Locale.ROOT).contains("who are you") -> "I'm a Q&A assistant demo running locally."
            question.lowercase(Locale.ROOT).contains("help") -> "Try asking about features or say hello."
            else -> "Ask another question or specify details."
        }
        val ref = "Turn #$count"
        val checksum = Base64.encodeToString(question.toByteArray(), Base64.NO_WRAP).take(6)
        return "$base\n\n$tip\n\n$ref • id:$checksum"
    }
}

/**
 * PUBLIC_INTERFACE
 * ApiResult is a simple sealed-like structure representing network outcomes.
 */
sealed class ApiResult {
    data class Success(val answer: String) : ApiResult()
    data class Error(val message: String) : ApiResult()
}
