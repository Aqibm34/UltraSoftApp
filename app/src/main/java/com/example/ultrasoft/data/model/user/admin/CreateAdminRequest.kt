package com.example.ultrasoft.data.model.user.admin

data class CreateAdminRequest(
    val emailId: String,
    val name: String,
    val phoneNumber: String,
    val password: String
)