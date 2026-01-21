package com.nikita.userservice.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
@Table(name = "messages")
data class Message(
    val text: String,
    @Enumerated(EnumType.STRING)
    val sentiment: Sentiment = Sentiment.UNSPECIFIED,
    @Id
    val id: String? = null
)
