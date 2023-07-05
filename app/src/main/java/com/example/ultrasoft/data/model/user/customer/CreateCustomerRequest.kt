package com.example.ultrasoft.data.model.user.customer

data class CreateCustomerRequest(
    val customerEmail: String,
    val customerName: String,
    val customerMobile: String,
    val password: String
)