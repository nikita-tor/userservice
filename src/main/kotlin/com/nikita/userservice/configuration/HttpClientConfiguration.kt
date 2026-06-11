package com.nikita.userservice.configuration

import com.nikita.userservice.util.GEMINI_CLIENT_NAME
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class HttpClientConfiguration {
    @Bean
    @Qualifier(GEMINI_CLIENT_NAME)
    fun restClient(builder: RestClient.Builder): RestClient = builder.build()
}
