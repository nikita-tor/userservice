// MessageService.kt
package com.nikita.userservice.service

import com.nikita.userservice.model.Message
import com.nikita.userservice.model.Sentiment
import com.nikita.userservice.repository.MessageRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val repository: MessageRepository,
    private val llmService: LlmService
) {
    fun findMessages(): List<Message> = repository.findAll().toList()

    fun findMessageById(id: String): Message? = repository.findById(id).orElse(null)

    fun save(message: Message): Message {
        var messageToSave = message

        if (messageToSave.sentiment == Sentiment.HAPPY) {
            val isHappy = llmService.checkSentiment(messageToSave.text)
            if (!isHappy) {
                 messageToSave = messageToSave.copy(sentiment = Sentiment.UNSPECIFIED)
            }
        }

        return if (messageToSave.id == null) {
            val newMessage = messageToSave.copy(id = UUID.randomUUID().toString())
            repository.save(newMessage)
        } else {
            repository.save(messageToSave)
        }
    }
}
