package com.nikita.userservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class HttpClientConfiguration {
    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }
}
