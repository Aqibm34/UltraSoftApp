package com.example.ultrasoft.data.model.user.engineer

data class CreateEngineerRequest(
    val engineerEmailId: String,
    val engineerName: String,
    val engineerMobile: String,
    val engineerWorkDiscription: String,
    val engineerPassword: String
)