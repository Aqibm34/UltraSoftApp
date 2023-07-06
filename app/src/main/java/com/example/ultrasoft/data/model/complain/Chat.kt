package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val id: Int,
    val remark: String,
    val attachment: String,
    val role: String,
    val admindetails: String?,
    val engineerDetails: String?,
    val createdDate: String,
) : Parcelable