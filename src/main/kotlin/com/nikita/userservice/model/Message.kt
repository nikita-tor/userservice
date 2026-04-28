package com.nikita.userservice.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "messages")
data class Message(
    val text: String,
    val sentiment: Sentiment? = null,
    @Id
    val id: String? = null,
)
