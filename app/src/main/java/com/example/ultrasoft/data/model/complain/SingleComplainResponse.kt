package com.example.ultrasoft.data.model.complain

data class SingleComplainResponse(
    val `data`: ComplainData,
    val message: String,
    val status_code: Int
)