package com.nikita.userservice.service

import com.nikita.userservice.model.Sentiment
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

private val logger = KotlinLogging.logger {}

@Service
class GeminiService(
    private val restClient: RestClient,
    @Value("\${gemini.api.key:}") private val apiKey: String,
) {
    private val baseUrl =
        "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent"

    fun analyzeSentiment(text: String): Sentiment {
        if (apiKey.isBlank()) {
            logger.warn { "Gemini API key is not configured. Defaulting to UNKNOWN." }
            return Sentiment.UNKNOWN
        }

        return try {
            val response =
                restClient
                    .post()
                    .uri(baseUrl)
                    .header("x-goog-api-key", apiKey)
                    .body(
                        GeminiRequest(
                            listOf(
                                Content(
                                    listOf(
                                        Part(
                                            "Analyze the sentiment of this message: \"$text\". Respond with one word only: HAPPY or UNKNOWN.",
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ).retrieve()
                    .body<GeminiResponse>()

            val resultText =
                response
                    ?.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text
                    ?.trim()
                    ?.uppercase()
            logger.info { "Gemini response: $resultText" }

            when (resultText) {
                "HAPPY" -> Sentiment.HAPPY
                else -> Sentiment.UNKNOWN
            }
        } catch (e: Exception) {
            logger.error(e) { "Error calling Gemini API. Defaulting to UNKNOWN." }
            Sentiment.UNKNOWN
        }
    }
}

data class GeminiRequest(
    val contents: List<Content>,
)

data class Content(
    val parts: List<Part>,
)

data class Part(
    val text: String,
)

data class GeminiResponse(
    val candidates: List<Candidate>,
)

data class Candidate(
    val content: Content,
)
