package com.example.ultrasoft.utility

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.abedelazizshe.lightcompressorlibrary.config.StorageConfiguration
import java.util.*

class MediaUtils {
    companion object {
        const val VIDEO_FORMAT = ".mp4"
        const val IMAGE_FORMAT = ".jpg"
        const val IMAGE_VIDEO_MIME = "image/* video/*"
        const val IMAGE_MIME = "image/*"
        const val VIDEO_MIME = "video/*"
        fun getVideoCompressorConfig() =
            Configuration(
                quality = VideoQuality.LOW,
                isMinBitrateCheckEnabled = false,
//                videoBitrate = 3677198, /*Int, ignore, or null*/
                disableAudio = false, /*Boolean, or ignore*/
                keepOriginalResolution = false, /*Boolean, or ignore*/
                videoWidth = 360.0, /*Double, ignore, or null*/
                videoHeight = 480.0 /*Double, ignore, or null*/
            )
//
        fun getVideoCompressorStorageConfig(fileName: String) = StorageConfiguration(
            saveAt = Environment.DIRECTORY_MOVIES, // => the directory to save the compressed video(s). Will be ignored if isExternal = false.
            isExternal = true, // => false means save at app-specific file directory. Default is true.
            fileName = "ym_complain_compressed.mp4" // => an optional value for a custom video name.
        )

         fun getMimeType(context: Context,uri: Uri): String? {
            val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                val cr: ContentResolver = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString().replace(" ",""))
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.lowercase(Locale.getDefault())
                )
            }
            return mimeType
        }
    }

}