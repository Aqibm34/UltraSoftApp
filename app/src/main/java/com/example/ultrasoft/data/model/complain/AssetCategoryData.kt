package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssetCategoryData(
    val assetCategoryId: String?,
    val assetCategoryName: String,
    val createdDate: String,
    val updatedDate: String
) : Parcelable