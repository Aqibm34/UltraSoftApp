package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssignedByAdminData(
    val activeStatus: String,
    val adminId: String,
    val createdDate: String,
    val emailId: String?,
    val name: String?,
    val password: String,
    val phoneNumber: String,
    val updatedDate: String?
) : Parcelable