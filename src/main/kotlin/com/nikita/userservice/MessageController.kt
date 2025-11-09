// MessageController.kt
package com.nikita.userservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@RestController
class MessageController {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String): String {
        logger.info { "index called with name: $name" }
        return "Hello, $name!"
    }
}