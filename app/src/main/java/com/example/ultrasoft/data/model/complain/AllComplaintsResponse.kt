package com.example.ultrasoft.data.model.complain

data class AllComplaintsResponse(
    val `data`: List<ComplainData>,
    val message: String,
    val status_code: Int
)