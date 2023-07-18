package com.example.ultrasoft.data.model.complain

data class ComplaintsCountResposne(
    val `data`: ComplainCountData,
    val message: String,
    val status_code: Int
)