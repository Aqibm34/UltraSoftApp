package com.example.ultrasoft.data.model.user.admin

data class AllAdminResponse(
    val `data`: List<AdminData>,
    val message: String,
    val status_code: Int
)