// MessageService.kt
package com.nikita.userservice.service

import com.nikita.userservice.model.Message
import com.nikita.userservice.repository.MessageRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class MessageService(private val repository: MessageRepository) {
    fun findMessages(): List<Message> = repository.findAll().toList()

    fun findMessageById(id: String): Message? = repository.findById(id).orElse(null)

    fun save(message: Message): Message {
        return if (message.id == null) {
            val newMessage = message.copy(id = UUID.randomUUID().toString())
            repository.save(newMessage)
        } else {
            repository.save(message)
        }
    }
}