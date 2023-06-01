package com.example.ultrasoft.data.model.user.admin

data class CreateAdminResponse(
    val `data`: AdminData,
    val message: String,
    val status_code: Int
)