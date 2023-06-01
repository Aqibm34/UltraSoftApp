package com.example.ultrasoft.data.model.user.engineer

data class AllEngineerResponse(
    val `data`: List<EngineerData>,
    val message: String,
    val status_code: Int
)