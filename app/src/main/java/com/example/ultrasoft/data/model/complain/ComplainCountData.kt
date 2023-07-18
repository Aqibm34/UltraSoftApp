package com.example.ultrasoft.data.model.complain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComplainCountData(
    var CLOSED: Int,
    var IN_PROGRESS: Int,
    var RESOLVED: Int,
    var UN_ASSIGNED: Int
):Parcelable