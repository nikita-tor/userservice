package com.nikita.userservice.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.io.File
import jakarta.annotation.PostConstruct

@Service
class LlmService(private val restClient: RestClient) {

    @Value("\${llm.api.key.path:llm_api_key.txt}")
    private lateinit var apiKeyPath: String

    @Value("\${llm.api.url:https://api.openai.com/v1/responses}")
    private lateinit var apiUrl: String

    // Model to use. Can be overridden.
    @Value("\${llm.model:gpt-4o-mini}")
    private lateinit var model: String

    private var cachedApiKey: String? = null

    @PostConstruct
    fun init() {
        // Attempt to load API key on startup, but don't fail if not present yet
        cachedApiKey = loadApiKey()
    }

    fun checkSentiment(message: String): Boolean {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            println("No API key found, skipping sentiment check and assuming no match.")
            return false
        }

        // Using 'input' with a list of messages as per "Simple message inputs are compatible" guidance for migration.
        val request = OpenAiRequest(
            model = model,
            input = listOf(
                OpenAiMessageInput(role = "user", content = "is the message sentiment happy? Respond with only \"yes\" or \"no\". Message: \"$message\"")
            )
        )

        try {
            val response = restClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer $apiKey")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OpenAiResponse::class.java)

            // Parsing the new response structure: output -> [item] -> content -> [part] -> text
            val content = response?.output?.firstOrNull()?.content?.firstOrNull()?.text?.trim()?.lowercase()
            return content == "yes"
        } catch (e: Exception) {
            println("Error calling LLM API: ${e.message}")
            return false
        }
    }

    private fun getApiKey(): String {
        if (cachedApiKey.isNullOrBlank()) {
            cachedApiKey = loadApiKey()
        }
        return cachedApiKey ?: ""
    }

    private fun loadApiKey(): String {
        val file = File(apiKeyPath)
        return if (file.exists()) {
            file.readText().trim()
        } else {
            ""
        }
    }
}

data class OpenAiRequest(
    val model: String,
    val input: List<OpenAiMessageInput>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiMessageInput(
    val role: String,
    val content: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiResponse(
    val output: List<OpenAiResponseOutputItem>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiResponseOutputItem(
    val content: List<OpenAiResponseContentPart>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiResponseContentPart(
    val type: String?,
    val text: String?
)
