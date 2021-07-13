package com.example.khind.model

data class ResponseLogin(val data: Data)

data class Data(val id: String, val email: String, val token: Token)

data class Token(val token: String, val expired_at: String, val refresh_token: String)
