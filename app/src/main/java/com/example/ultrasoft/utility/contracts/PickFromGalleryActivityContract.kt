package com.example.ultrasoft.utility.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.ultrasoft.utility.MediaUtils
import com.example.ultrasoft.utility.Utils
import com.example.ultrasoft.utility.logE

class PickFromGalleryActivityContract : ActivityResultContract<String, Uri?>() {
    private var context: Context? = null
    private val tag = PickFromGalleryActivityContract::class.java.simpleName
    override fun createIntent(context: Context, input: String): Intent {
        this.context = context

        return Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
        ).apply { type = input }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            return try {
                val mimeType = intent?.data?.let { MediaUtils.getMimeType(context!!, it) }
                if (mimeType != null) {
                    return if (mimeType.contains("image")) {
                        val filePath = intent.data?.let { Utils.getPaths(context!!, it) }
                        Uri.parse(filePath)
                    } else {
                        intent.data //video compressor requires content:// uri
                    }
                }
                null
            } catch (e: Exception) {
                logE(tag,e.message)
                null
            }
        }
        return null
    }
}