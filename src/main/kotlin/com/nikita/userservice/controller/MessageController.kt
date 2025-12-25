// MessageController.kt
package com.nikita.userservice.controller

import com.nikita.userservice.model.Message
import com.nikita.userservice.service.MessageService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/")
class MessageController(private val service: MessageService) {
    @GetMapping
    fun listMessages(): ResponseEntity<List<Message>> {
        logger.info("Calling list messages endpoint")
        return ResponseEntity.ok(service.findMessages())
    }

    @PostMapping
    fun post(@RequestBody message: Message): ResponseEntity<Message> {
        logger.info("Saving message with value: ${message.toString()}")
        val savedMessage = service.save(message)
        
        logger.info("Saved message successfully. Message id: ${savedMessage.id}")
        return ResponseEntity.created(URI("/${savedMessage.id}")).body(savedMessage)
    }

    @GetMapping("/{id}")
    fun getMessage(@PathVariable id: String): ResponseEntity<Message> =
        service.findMessageById(id).toResponseEntity()

    private fun Message?.toResponseEntity(): ResponseEntity<Message> =
        // If the message is null (not found), set response code to 404
        this?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
}