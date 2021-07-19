package com.example.khind.model

data class ResponseMessage(val data: ArrayList<Message>)

data class Message(
    val id: String,
    val title: String,
    val description: String,
    val photo: String,
    val created_at: Long
)
