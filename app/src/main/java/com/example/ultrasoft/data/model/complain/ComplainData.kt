package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComplainData(
    val assignedByAdminId: String?,
    val assignedByAdminName: String?,
    val assignedToEngineerId: String?,
    val assignedToEngineerName: String?,
    val chats: List<Chat>,
    val complaintId: String,
    val createdByCustomerId: String,
    val createdByCustomerName: String,
    val createdDate: String,
    val status: String
) : Parcelable