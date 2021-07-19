package com.example.khind.model

import java.io.Serializable

data class ResponseLogin(val data: Data)

data class Data(val id: String, val email: String, val token: Token)

data class RefreshToken(val data: ReData)

data class ReData(val token: Token)

data class Token(
    val token: String,
    val expired_at: Long,
    val refresh_token: String
): Serializable
