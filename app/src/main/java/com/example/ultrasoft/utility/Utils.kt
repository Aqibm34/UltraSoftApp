package com.example.ultrasoft.utility

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.example.ultrasoft.R
import java.io.*


class Utils {

    companion object {
//        val imageChooseIntent =
//            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        val videoChooseIntent = Intent(Intent.ACTION_PICK).setDataAndType(
//            MediaStore.Video.Media.INTERNAL_CONTENT_URI,
//            "video/*"
//        )

        fun isConnectingToInternet(mContext: Context): Boolean {
            val connectivity =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.allNetworkInfo
            for (networkInfo in info) if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                return true
            }
            return false
        }

//        fun convertDateToZ(): String {
//            return try {
//                val f = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//                f.timeZone = TimeZone.getTimeZone("UTC")
//                f.format(Date())
//            } catch (e: Exception) {
//                "NA";
//            }
//        }


//        var textWithSpaceFilter =
//            InputFilter { source: CharSequence?, _: Int, _: Int, _: Spanned?, _: Int, _: Int ->
//                val regexCheck: Pattern = Pattern.compile("^[a-zA-Z0-9\\s]*$")
//                if (source != null && !regexCheck.matcher(source).matches()) {
//                    return@InputFilter ""
//                }
//                null
//            }
//
//        var textWithNoSpaceFilter =
//            InputFilter { source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int ->
//                val regexCheck: Pattern = Pattern.compile("^[a-zA-Z0-9]*$")
//                if (source != null && !regexCheck.matcher(source).matches()) {
//                    return@InputFilter ""
//                }
//                null
//            }


        fun setCaptureFileUri(
            context: Context,
            imageFileName: String,
            extension: String = MediaUtils.IMAGE_FORMAT
        ): List<Uri>? {
            val list = mutableListOf<Uri>()
            var file: File? = null
            try {
                file = saveCaptureImageFile(context, imageFileName, extension)
            } catch (ignored: Exception) {
            }
            if (file != null) {
                list.add(Uri.fromFile(file)) //File access uri
                try {
                    //Intent uri
                    list.add(
                        FileProvider.getUriForFile(
                            context, "com.example.ultrasoft.provider", file
                        )
                    )
                    return list
                } catch (e: Exception) {
                    context.toast("FileException" + e.message)
                }
            }
            return null
        }

        private fun saveCaptureImageFile(
            context: Context,
            imageFileName: String,
            extension: String
        ): File? {
            val storageDir: File? =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                imageFileName,
                extension,
                storageDir
            )
        }


        fun compressImageFile(file: File, context: Context): File {
            val f = File(context.filesDir, file.name)
            try {
                f.createNewFile()
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

            var bitmap = BitmapFactory.decodeFile(file.path)
            val bos = ByteArrayOutputStream()
            bitmap = getRotateImage(file.path, bitmap)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos)
            val bitmapdata: ByteArray = bos.toByteArray()
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(f)
            } catch (e: FileNotFoundException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            try {
                fos?.write(bitmapdata)
                fos?.flush()
                fos?.close()
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            return f
        }


        private fun getRotateImage(photoPath: String?, bitmap: Bitmap?): Bitmap? {
            try {
                val ei = ExifInterface(photoPath!!)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap: Bitmap? = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                        bitmap!!, 90
                    )
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                        bitmap!!, 180
                    )
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                        bitmap!!, 270
                    )
                    ExifInterface.ORIENTATION_NORMAL -> bitmap
                    else -> bitmap
                }
                return rotatedBitmap
            } catch (e: Exception) {
                logE("Bitmap Rotate", e.message)
                return bitmap
            }
        }


        fun getPaths(context: Context, uri: Uri): String {
            var result: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val colIndex: Int = cursor.getColumnIndexOrThrow(proj[0])
                    result = cursor.getString(colIndex)
                }
                cursor.close()
            }
            if (result == null) {
                result = "Not found"
            }
            return result
        }


//        fun parseDateWithTimeZone(time: String, format: String = ""): String {
//            return try {
//                var frmt = "yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS"
//                if (format.isNotEmpty()) {
//                    frmt = format
//                }
//
//                var dateFormatter =
//                    SimpleDateFormat(frmt, Locale.getDefault())
//                dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
//                val utcDate = dateFormatter.parse(time)
//
//                dateFormatter = SimpleDateFormat("dd MMM yy, hh:mm a", Locale.getDefault())
//                dateFormatter.timeZone = TimeZone.getDefault()
//                if (utcDate != null) {
//                    dateFormatter.format(utcDate)
//                } else {
//                    "NA"
//                }
//            } catch (e: java.lang.Exception) {
//                e.message.toString()
//            }
//        }
//
//        fun formatDate(time: String, format: String = ""): String {
//            return try {
//                var frmt = "yyyy-MM-dd"
//                if (format.isNotEmpty()) {
//                    frmt = format
//                }
//
//                var dateFormatter =
//                    SimpleDateFormat(frmt, Locale.getDefault())
//                dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
//                val utcDate = dateFormatter.parse(time)
//
//                dateFormatter = SimpleDateFormat("dd MMM yy", Locale.getDefault())
//                dateFormatter.timeZone = TimeZone.getDefault()
//                if (utcDate != null) {
//                    dateFormatter.format(utcDate)
//                } else {
//                    "NA"
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//                "NA"
//            }
//        }
//
//
//        fun getCurrentMonthStartEndDate(): Pair<String, String> {
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.MONTH, 0)
//            calendar[Calendar.DATE] = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
//            val monthFirstDay = calendar.time
//            val monthCurrentDay = Calendar.getInstance().time
//
//            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            val startDateStr = df.format(monthFirstDay)
//            val endDateStr = df.format(monthCurrentDay)
//            return Pair(startDateStr, endDateStr)
//        }
//
//        fun getCurrentDayOfMonth(): Int {
//            val monthCurrentDay = Calendar.getInstance().time
//            val df = SimpleDateFormat("dd", Locale.getDefault())
//            val endDateStr = df.format(monthCurrentDay)
//            return endDateStr.toInt()
//        }
//
//        fun getLastDayOfMonth(): Int =
//            Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
//
//
//        fun getLast7DaysDate(): kotlin.Pair<String, String> {
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.DAY_OF_YEAR, -7)
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            val startDate = dateFormat.format(calendar.time)
//            val currentTime = Calendar.getInstance().time
//            val currentDate = dateFormat.format(currentTime)
//            return kotlin.Pair(startDate, currentDate)
//        }

        fun loadPictureWithName(context: Context, fileName: String?, iv: ImageView) {
            Glide.with(context).load("EMP_IMAGES_URL" + fileName)
                .placeholder(R.drawable.image_placeholder).into(iv)
        }

        fun loadPicture(context: Context, url: String?, iv: ImageView) {
            Glide.with(context).load(url)
                .placeholder(R.drawable.image_placeholder).into(iv)
        }

        fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
    }


}