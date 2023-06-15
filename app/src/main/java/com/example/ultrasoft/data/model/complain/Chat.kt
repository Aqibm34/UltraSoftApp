package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val adminId: String?,
    val adminName: String?,
    val attachment: String,
    val engId: String?,
    val engName: String?,
    val messageBy: String?,
    val remarks: String?
) : Parcelable