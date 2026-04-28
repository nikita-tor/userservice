// MessageService.kt
package com.nikita.userservice.service

import com.nikita.userservice.model.Message
import com.nikita.userservice.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService(
    private val repository: MessageRepository,
    private val geminiService: GeminiService,
) {
    fun findMessages(): List<Message> = repository.findAll().toList()

    fun findMessageById(id: String): Message? = repository.findById(id).orElse(null)

    fun save(message: Message): Message {
        val sentiment = message.sentiment ?: geminiService.analyzeSentiment(message.text)
        val messageToSave =
            if (message.id == null) {
                message.copy(id = UUID.randomUUID().toString(), sentiment = sentiment)
            } else {
                message.copy(sentiment = sentiment)
            }
        return repository.save(messageToSave)
    }
}
