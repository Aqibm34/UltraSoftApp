package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatedByCustomerData(
    val customerAddress:  String?,
    val customerGSTNumber:  String?,
    val customerCity:  String?,
    val customerState:  String?,
    val customerMobile: String,
    val customerName: String,
    val customerEmail: String,
    val activeStatus: String,
    val password: String,
    val createdDate: String,
    val customerId: String,
    val isFirstTimeLogin: String,
    val updatedDate: String
) : Parcelable