package com.example.ultrasoft.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ultrasoft.BuildConfig
import com.example.ultrasoft.R
import com.example.ultrasoft.databinding.CustomAlertDialogBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast.show()
}

fun Context.toast(msg: String, length: Int) {
    Toast.makeText(this, msg, length).show()
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}


fun Any.logE(tag: String, message: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, if (message.isNullOrEmpty()) "NULL" else message)
    }
}

fun Any.logD(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun Any.logLarge(tag: String?, str: String) {
    if (BuildConfig.DEBUG) {
        if (str.length > 3000) {
            Log.e(tag, str.substring(0, 3000))
            logLarge(tag, str.substring(3000))
        } else {
            Log.e(tag, str)
        }
    }
}

fun Context.isNetworkConnected(): Boolean {
    val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (SDK_INT >= Q)
        manager.getNetworkCapabilities(manager.activeNetwork)?.let {
            it.hasTransport(TRANSPORT_WIFI) || it.hasTransport(TRANSPORT_CELLULAR) ||
                    it.hasTransport(TRANSPORT_BLUETOOTH) ||
                    it.hasTransport(TRANSPORT_ETHERNET) ||
                    it.hasTransport(TRANSPORT_VPN)
        } ?: false
    else
        @Suppress("DEPRECATION")
        manager.activeNetworkInfo?.isConnected == true
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.createPartFromString(string: String): RequestBody {
    return string.toRequestBody(MultipartBody.FORM)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.prepareImageFilePart(partName: String, file: File): MultipartBody.Part {
    val requestBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

fun Fragment.prepareVideoFilePart(partName: String, file: File): MultipartBody.Part {
    val requestBody: RequestBody = file.asRequestBody("video/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

fun Context.prepareFilePart(partName: String, file: File): MultipartBody.Part {
    val requestBody: RequestBody =
        file.asRequestBody(contentResolver.getType(Uri.fromFile(file))?.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}


fun Fragment.showAlert(
    contentText: String?,
    alertType: AppConstants.AlertType,
    title: String = "",
    listener: (response: AppConstants.AlertResponseType) -> Unit
) {
    val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
    val v = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
    val btnConfirm: TextView = v.findViewById(R.id.btnConfirm)
    val btnCancel: TextView = v.findViewById(R.id.btnCancel)
    val tvTitle: TextView = v.findViewById(R.id.tvTitle)
    val tvText: TextView = v.findViewById(R.id.tvText)
    val iv: ImageView = v.findViewById(R.id.iv)

    var finalTitle = title
    when (alertType) {
        AppConstants.AlertType.SUCCESS -> {
            if (title.isEmpty()) {
                finalTitle = "Success"
            }
            iv.setBackgroundResource(R.drawable.ic_success)
        }
        AppConstants.AlertType.ERROR -> {
            if (title.isEmpty()) {
                finalTitle = "Error"
            }
            iv.setBackgroundResource(R.drawable.ic_error)
        }
        AppConstants.AlertType.INFO -> {
            iv.setBackgroundResource(R.drawable.ic_info)
        }
    }

    tvTitle.text = finalTitle
    tvText.text = contentText

    builder.setView(v)
    btnConfirm.setOnClickListener {
        listener(AppConstants.AlertResponseType.YES)
        builder.dismiss()
    }
    btnCancel.setOnClickListener {
        listener(AppConstants.AlertResponseType.NO)
        builder.dismiss()
    }
    builder.setCancelable(false)
    builder.show()
}

fun Fragment.showAlertWithButtonConfig(
    context: Context,
    title: String,
    contentText: String?,
    alertType: AppConstants.AlertType,
    cancelBtnText: String = "Cancel",
    confirmBtnText: String = "Cancel",
    showCancel: Boolean = true,
    listener: (response: AppConstants.AlertResponseType) -> Unit
) {
    val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
    val dBinding = CustomAlertDialogBinding.inflate(layoutInflater)
    builder.setView(dBinding.root)

    dBinding.tvTitle.text = title
    dBinding.tvText.text = contentText
    dBinding.btnCancel.text = cancelBtnText
    dBinding.btnConfirm.text = confirmBtnText
    dBinding.btnCancel.visibility = if (showCancel) View.VISIBLE else View.GONE
    when (alertType) {
        AppConstants.AlertType.SUCCESS -> {
            dBinding.iv.setBackgroundResource(R.drawable.ic_success)
        }
        AppConstants.AlertType.ERROR -> {
            dBinding.iv.setBackgroundResource(R.drawable.ic_error)
        }
        AppConstants.AlertType.INFO -> {
            dBinding.iv.setBackgroundResource(R.drawable.ic_info)
        }
    }

    dBinding.btnConfirm.setOnClickListener {
        listener(AppConstants.AlertResponseType.YES)
        builder.dismiss()
    }
    dBinding.btnCancel.setOnClickListener {
        listener(AppConstants.AlertResponseType.NO)
        builder.dismiss()
    }
    builder.setCancelable(false)
    builder.show()
}

fun Activity.showAlert(
    context: Context,
    title: String,
    contentText: String?,
    alertType: AppConstants.AlertType,
    listener: (response: AppConstants.AlertResponseType) -> Unit
) {
    val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
    val v = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
    val btnConfirm: TextView = v.findViewById(R.id.btnConfirm)
    val btnCancel: TextView = v.findViewById(R.id.btnCancel)
    val tvTitle: TextView = v.findViewById(R.id.tvTitle)
    val tvText: TextView = v.findViewById(R.id.tvText)
    val iv: ImageView = v.findViewById(R.id.iv)

    tvTitle.text = title
    tvText.text = contentText
    when (alertType) {
        AppConstants.AlertType.SUCCESS -> {
            iv.setBackgroundResource(R.drawable.ic_success)
        }
        AppConstants.AlertType.ERROR -> {
            iv.setBackgroundResource(R.drawable.ic_error)
        }
        AppConstants.AlertType.INFO -> {
            iv.setBackgroundResource(R.drawable.ic_info)
        }
    }

    builder.setView(v)
    btnConfirm.setOnClickListener {
        listener(AppConstants.AlertResponseType.YES)
        builder.dismiss()
    }
    btnCancel.setOnClickListener {
        listener(AppConstants.AlertResponseType.NO)
        builder.dismiss()
    }
    builder.setCancelable(false)
    builder.show()
}

fun Fragment.loadImage(iv: ImageView, url: String) {
    Glide.with(requireContext())
        .load(url)
        .into(iv)
}

fun Fragment.loadImagePreview(uri: Uri, iv: ImageView) {
    Glide.with(this)
        .load(uri)
        .into(iv)
}

fun Fragment.loadImageOrVideoPreview(uri: Uri, iv: ImageView) {
    iv.visibility = View.VISIBLE
    if (MediaUtils.getMimeType(requireContext(), uri)?.contains("video") == true) {
        Glide.with(this).load(uri).into(iv)
    } else {
        Glide.with(this).load(uri.path).into(iv)
    }
}

fun Any.logLargeString(tag: String, str: String) {
    if (str.length > 3000) {
        Log.e(tag, str.substring(0, 3000))
        logLargeString(tag, str.substring(3000))
    } else {
        Log.e(tag, str)
    }
}

fun Fragment.formatDate(date: Date): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        logE("Exception Format", e.toString())
        "00/00/0000"
    }
}

fun Fragment.formatDateToMonthName(date: Date): String {
    return try {
        val format = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        logE("Exception Format", e.toString())
        "00/00/0000"
    }
}

fun Fragment.formatDate(date: String): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.parse(date)
    } catch (e: Exception) {
        logE("Exception Parse", e.toString())
        Date()
    }
}

fun Any.formatAmountWithSymbol(amount: Double): String {
    return try {
        val intAmount = "%.0f".format(amount).toInt()
        "₹ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(intAmount)
    } catch (e: Exception) {
        ""
    }
}

fun Double.formatAmount(): String {
    var value = this
    if (this < 0) {
        value = this * -1
    }
    return try {
        val intAmount = "%.0f".format(value).toInt()
        NumberFormat.getNumberInstance(Locale.getDefault()).format(intAmount)
    } catch (e: Exception) {
        ""
    }
}

fun Fragment.formatDate(date: String, dateFormat: String): Date? {
    return try {
        val format = SimpleDateFormat(dateFormat, Locale.getDefault())
        format.parse(date)
    } catch (e: Exception) {
        logE("Exception", e.toString())
        Date()
    }
}

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { str ->
        str.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}

enum class SnackTypes { Error, Info, Success }

fun View.showSnackBar(
    message: String?,
    type: SnackTypes,
    length: Int = Snackbar.LENGTH_SHORT,
    action: (Snackbar.() -> Unit)? = null
) {

    val snackBar = Snackbar.make(this, message ?: "Null", length)
    action?.let { snackBar.it() }
    val view = snackBar.view
    view.setBackgroundResource(R.drawable.snackbar_bg)
    var textColor = R.color.white
    val color: Int = when (type) {
        SnackTypes.Success -> {
            R.color.green
        }
        SnackTypes.Error -> {
            R.color.red
        }
        SnackTypes.Info -> {
            textColor = R.color.text_black
            R.color.yellow
        }
    }

    view.backgroundTintList = ContextCompat.getColorStateList(context, color)
    val snackBarTextView =
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    snackBarTextView.setTextColor(ContextCompat.getColor(context, textColor))
    snackBarTextView.setTypeface(null, Typeface.BOLD)
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params
    snackBar.show()
}

fun Snackbar.action(message: String, action: (View) -> Unit) {
    this.setAction(message, action)
}

fun Fragment.getPreviousMonth(before: Int): Triple<String, String, String> {
    val cal: Calendar = Calendar.getInstance() //Get current date/month
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val monthYearFormat = SimpleDateFormat("MMM-yy", Locale.getDefault())

    cal.add(Calendar.MONTH, before) //Go to date, 6 months
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH)) // set day 1
    val firstDay = dateFormat.format(cal.time)

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)) // set day last
    val lastDay = dateFormat.format(cal.time)
    val monthAndYear = monthYearFormat.format(cal.time)

    return Triple(firstDay, lastDay, monthAndYear)
}

fun Any.getDayNumFromDate(date: String): String {
    return try {
        val sdfParse = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val d: Date = sdfParse.parse(date)!!
        val sdfFormat = SimpleDateFormat("d", Locale.getDefault())
        sdfFormat.format(d)
    } catch (e: Exception) {
        "NA"
    }
}

fun Any.getDayNameFromDate(date: String): String {
    return try {
        val sdfParse = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val d: Date = sdfParse.parse(date)!!
        val sdfFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        sdfFormat.format(d)
    } catch (e: Exception) {
        "NA"
    }
}

fun String.formatTime(format: String = "HH:mm:ss"): String {
    return try {
        var dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        val date = dateFormatter.parse(this)
        dateFormatter = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        if (date != null) {
            dateFormatter.format(date)
        } else {
            "Null"
        }
    } catch (e: Exception) {
        e.message.toString()
    }
}


fun String.formatIstDateTime(): String {
    val dateArray = this.split(" ")
    val date = "${dateArray[0]} ${dateArray[1]} ${dateArray[2]}"
    val time = dateArray[3]
    return "$date, ${time.formatTime()}"
}

fun String.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()






