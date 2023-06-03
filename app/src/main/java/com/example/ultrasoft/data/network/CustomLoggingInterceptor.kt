package com.example.ultrasoft.data.network

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.ultrasoft.ui.activity.MainActivity
import com.example.ultrasoft.utility.logE
import com.example.ultrasoft.utility.toast
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.IOException
import org.json.JSONObject
import javax.inject.Inject

class CustomLoggingInterceptor @Inject constructor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var requestLog = String.format(
            "Request Sent %s on %s%n%s",
            request.url,
            chain.connection(),
            request.headers
        )

        if (request.method.compareTo("post", ignoreCase = true) == 0) {
            requestLog = "\n$requestLog\nBODY:" + bodyToString(request)
        }

        logE("REQUEST::", requestLog)

        val response = chain.proceed(request)
        val responseLog =
            String(if (response.body != null) response.body!!.bytes() else byteArrayOf())

        try {
            if (JSONObject(responseLog).getString("message")
                    .equals("Invalid session", ignoreCase = true)
            ) {
                Handler(Looper.getMainLooper()).post {
                    context.toast("Invalid session")
                }
                context.getSharedPreferences("app_prefs_ultrasoft", Context.MODE_PRIVATE)
                    .edit().clear().apply()
                context.startActivity(
                    Intent(context, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        } catch (ignored: Exception) {
            logE("Redirect Exception", ignored.message)
        }

        logE("RESPONSE :: "+response.code, responseLog)

        return response.newBuilder()
            .body(responseLog.toResponseBody(response.body!!.contentType()))
            .build()
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            String.format("REQUEST ERROR:(%s)", e)
        }
    }
}