package com.example.ultrasoft.data.model.user.customer

data class CreateCustomerResponse(
    val `data`: CustomerData,
    val message: String,
    val status_code: Int
)