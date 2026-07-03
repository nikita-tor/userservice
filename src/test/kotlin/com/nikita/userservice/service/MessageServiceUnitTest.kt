package com.nikita.userservice.service

import com.nikita.userservice.model.Message
import com.nikita.userservice.model.Sentiment
import com.nikita.userservice.repository.MessageRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class MessageServiceUnitTest {
    private val repository: MessageRepository = mock(MessageRepository::class.java)
    private val geminiService: GeminiService = mock(GeminiService::class.java)
    private val service = MessageService(repository, geminiService)

    @Test
    fun `should return all messages from repository`() {
        val dummyMessages = listOf(Message("dummy", Sentiment.HAPPY, "dummy-id"))
        org.mockito.Mockito
            .`when`(repository.findAll())
            .thenReturn(dummyMessages)

        val result = service.findMessages()

        assertEquals(1, result.size)
        assertEquals("dummy", result[0].text)
    }

    @Test
    fun `should return empty list when repository is empty`() {
        org.mockito.Mockito
            .`when`(repository.findAll())
            .thenReturn(emptyList())

        val result = service.findMessages()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return message when found by id`() {
        val message = Message("hello", Sentiment.HAPPY, "msg-1")
        org.mockito.Mockito
            .`when`(repository.findById("msg-1"))
            .thenReturn(java.util.Optional.of(message))

        val result = service.findMessageById("msg-1")

        assertNotNull(result)
    }

    @Test
    fun `should return null when not found by id`() {
        org.mockito.Mockito
            .`when`(repository.findById("msg-1"))
            .thenReturn(java.util.Optional.empty())

        val result = service.findMessageById("msg-1")

        assertNull(result)
    }

    @Test
    fun `should generate UUID and call Gemini when saving without id`() {
        val message = Message("Hello world", null, null)
        org.mockito.Mockito
            .`when`(geminiService.analyzeSentiment(message.text))
            .thenAnswer { Sentiment.HAPPY }
        org.mockito.Mockito
            .`when`(repository.save<Message>(org.mockito.ArgumentMatchers.any(Message::class.java)))
            .thenAnswer { invocation -> invocation.getArgument<Message>(0) }

        val result = service.save(message)

        assertNotNull(result.id)
        assertEquals(Sentiment.HAPPY, result.sentiment)
    }

    @Test
    fun `should not regenerate id when already present`() {
        val message = Message("Hello world", null, "existing-id")
        org.mockito.Mockito
            .`when`(geminiService.analyzeSentiment(message.text))
            .thenAnswer { Sentiment.UNKNOWN }
        org.mockito.Mockito
            .`when`(repository.save<Message>(org.mockito.ArgumentMatchers.any(Message::class.java)))
            .thenAnswer { invocation -> invocation.getArgument<Message>(0) }

        val result = service.save(message)

        assertEquals("existing-id", result.id)
    }
}

private fun <T> List<T>.asIterable(): Iterable<T> = this
