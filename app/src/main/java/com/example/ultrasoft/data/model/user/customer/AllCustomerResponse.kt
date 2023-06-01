package com.example.ultrasoft.data.model.user.customer

data class AllCustomerResponse(
    val `data`: List<CustomerData>,
    val message: String,
    val status_code: Int
)