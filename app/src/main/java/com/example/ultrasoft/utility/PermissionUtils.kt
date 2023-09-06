package com.example.ultrasoft.utility

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.hasGalleryPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context,
        READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasCameraPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context,
        CAMERA
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasWritePermission() =
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
        true
    } else {
        ContextCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

fun Any.hasNotificationPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context,
        POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED


fun Any.hasLocationPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context,
        ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


fun redirectToPermissionSettings(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}

