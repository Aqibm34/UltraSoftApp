package com.example.ultrasoft.data.model.login

data class LoginRequest(
    val username: String,
    val password: String,
    val role: String
)