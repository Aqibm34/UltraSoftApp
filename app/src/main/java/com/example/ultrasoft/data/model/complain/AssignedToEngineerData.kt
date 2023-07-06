package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssignedToEngineerData(
    val activeStatus: String,
    val createdDate: String,
    val engineerEmailId: String,
    val engineerId: String,
    val engineerMobile: String,
    val engineerName: String,
    val engineerWorkDiscription: String,
    val password: String,
    val updatedDate: String
) : Parcelable