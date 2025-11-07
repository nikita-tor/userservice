package com.nikita.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.client.RestClient
import lombok.extern.slf4j.Slf4j

@SpringBootApplication
@Slf4j
class UserserviceApplication

fun main(args: Array<String>) {
	runApplication<UserserviceApplication>(*args)
}
