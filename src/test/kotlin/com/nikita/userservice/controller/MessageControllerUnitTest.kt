package com.nikita.userservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nikita.userservice.model.Message
import com.nikita.userservice.model.Sentiment
import com.nikita.userservice.service.MessageService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.springframework.http.MediaType
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get as mvcGet
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post as mvcPost
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content as mockContent
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header as mockHeader
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status as mockStatus

@ExtendWith(MockitoExtension::class)
class MessageControllerUnitTest(
    @Mock private val messageService: MessageService,
) {
    private val objectMapper: ObjectMapper =
        ObjectMapper().registerModule(
            com.fasterxml.jackson.module.kotlin.KotlinModule
                .Builder()
                .build(),
        )

    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(MessageController(messageService)).build()

    companion object {
        private val DUMMY_MESSAGE = Message("Test message", Sentiment.HAPPY, "test-uuid")
        private val DUMMY_JSON = """{"text":"Test message","id":"test-uuid","sentiment":"HAPPY"}"""
    }

    @Test
    fun `GET all messages returns list`() {
        `when`(messageService.findMessages()).thenReturn(listOf(DUMMY_MESSAGE))

        mockMvc
            .perform(mvcGet("/api/v1/messages").accept(MediaType.APPLICATION_JSON))
            .andExpect(mockStatus().isOk)
            .andExpect(mockContent().json(objectMapper.writeValueAsString(listOf(DUMMY_MESSAGE)), JsonCompareMode.STRICT))
    }

    @Test
    fun `GET all messages returns empty list`() {
        `when`(messageService.findMessages()).thenReturn(emptyList())

        mockMvc
            .perform(mvcGet("/api/v1/messages").accept(MediaType.APPLICATION_JSON))
            .andExpect(mockStatus().isOk)
            .andExpect(mockContent().json("[]", JsonCompareMode.STRICT))
    }

    @Test
    fun `POST creates message with 201`() {
        val createRequest = Message("Create me", null, null)
        doReturn(DUMMY_MESSAGE).`when`(messageService).save(any())

        mockMvc
            .perform(
                mvcPost("/api/v1/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)),
            ).andExpect(mockStatus().isCreated)
            .andExpect(mockHeader().string("Location", "/api/v1/messages/test-uuid"))
            .andExpect(mockContent().json(DUMMY_JSON, JsonCompareMode.STRICT))
    }

    @Test
    fun `GET by id returns message when found`() {
        `when`(messageService.findMessageById("test-uuid")).thenReturn(DUMMY_MESSAGE)

        mockMvc
            .perform(mvcGet("/api/v1/messages/test-uuid").accept(MediaType.APPLICATION_JSON))
            .andExpect(mockStatus().isOk)
            .andExpect(mockContent().json(DUMMY_JSON, JsonCompareMode.STRICT))
    }

    @Test
    fun `GET by id returns 404 when not found`() {
        `when`(messageService.findMessageById("not-found")).thenReturn(null)

        mockMvc
            .perform(mvcGet("/api/v1/messages/not-found").accept(MediaType.APPLICATION_JSON))
            .andExpect(mockStatus().isNotFound)
    }

    @Test
    fun `POST handles invalid JSON gracefully`() {
        mockMvc
            .perform(
                mvcPost("/api/v1/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{invalid json}"),
            ).andExpect(mockStatus().isBadRequest)
    }
}
