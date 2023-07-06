package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComplainData(
    val complainId: String,
    val status: String,
    val assetCategory: AssetCategoryData,
    val assignedByAdmin: AssignedByAdminData?,
    val assignedToEngineer: AssignedToEngineerData?,
    val createdByCUstomer: CreatedByCustomerData,
    val complaintChats: List<Chat>,
    val createdDate: String,
    val ticketClosedDate: String?,
    val engineerAssignedDate: String?,
    val updatedDate: String
) : Parcelable