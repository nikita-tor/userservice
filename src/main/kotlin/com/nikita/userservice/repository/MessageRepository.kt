package com.nikita.userservice.repository

import com.nikita.userservice.model.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, String> // TODO: Rewrite with JpaRepository
