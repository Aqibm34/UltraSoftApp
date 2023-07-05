package com.example.ultrasoft.data.model.asset

data class AllAssetCategoryResponse(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
)