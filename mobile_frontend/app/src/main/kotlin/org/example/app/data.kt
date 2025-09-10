package org.example.app

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient as OkHttp3Client
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * PUBLIC_INTERFACE
 * Role defines the message authors used in the conversation list.
 */
enum class Role { User, Assistant, System }

/**
 * PUBLIC_INTERFACE
 * Message represents a chat message shown in the conversation.
 */
data class Message(
    val role: Role,
    val content: String
)

@JsonClass(generateAdapter = true)
data class AskRequest(
    @Json(name = "question")
    val question: String
)

@JsonClass(generateAdapter = true)
data class AskResponse(
    @Json(name = "answer")
    val answer: String
)

/**
 * PUBLIC_INTERFACE
 * QAService describes REST endpoints of the backend.
 *
 * Endpoint: POST /api/ask
 * Request: { "question": "<string>" }
 * Response: { "answer": "<string>" }
 */
interface QAService {
    @Headers("Content-Type: application/json")
    @POST("/api/ask")
    suspend fun ask(@Body body: AskRequest): AskResponse
}

/**
 * PUBLIC_INTERFACE
 * QARepository is the data source responsible for talking to QAService.
 */
class QARepository {

    // NOTE: For configuration, set BACKEND_BASE_URL via environment at build time.
    // The orchestrator should inject it; do not hardcode secrets.
    // If not provided, defaults to http://10.0.2.2:8000 for Android emulator loopback.
    private val baseUrl: String by lazy {
        val env = System.getenv("BACKEND_BASE_URL")
        (env?.ifBlank { null } ?: "http://10.0.2.2:8000").let {
            if (it.endsWith("/")) it.dropLast(1) else it
        }
    }

    private val service: QAService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttp3Client.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(QAService::class.java)
    }

    // PUBLIC_INTERFACE
    suspend fun ask(question: String): String {
        /** Calls backend to get an answer string for the question. */
        val res = service.ask(AskRequest(question = question))
        return res.answer
    }
}
