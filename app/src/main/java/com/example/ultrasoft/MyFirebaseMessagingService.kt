package com.example.ultrasoft

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.ultrasoft.data.model.NotificationData
import com.example.ultrasoft.ui.activity.MainActivity
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.AppPreferences
import com.example.ultrasoft.utility.logE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONArray
import java.io.IOException
import java.net.URL


class MyFireBaseMessagingService : FirebaseMessagingService() {
    private val fcmPicture = "picture"
    private val fcmStyle = "style"
    private val fcmList = "list"
    private val fcmText = "text"
    private val fcmTitle = "title"
    private val channelName = "FCM"
    private val channelDsc = "Firebase Cloud Messaging"
    private var numMessages = 0
    private val channelId = "UL Channel"

    private enum class Style { BigText, BigPicture, Inbox }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        AppConstants.FCM_TOKEN_REFRESHED = token
        logE("On Token Refreshed", token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        try {

            if (message.data.isNotEmpty()) {
                logE("FCM Payload", message.data.toString())
            }
            val appPreferences = AppPreferences.getInstance(applicationContext)
            val list = appPreferences.getNotificationList()
            list.add(
                0, NotificationData(
                    message.data[fcmTitle].toString(),
                    message.data[fcmText].toString(),
                    message.data[fcmPicture].toString()
                )
            )
            appPreferences.addDataInNotificationList(list)
            showNotification(message)

        } catch (e: Exception) {
            logE("onMessageReceived", e.message)
        }
    }

    private fun showNotification(message: RemoteMessage) {
//        val bundle = Bundle()
//        bundle.putString(fcmParam, data[fcmParam])
        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            flag
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.data[fcmTitle])
            .setContentText(message.data[fcmText])
            .setAutoCancel(true)
            .setNumber(numMessages)
            .setContentIntent(pendingIntent)
            .setContentInfo("Ultrasoft")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setAutoCancel(false)
            .setPriority(Notification.PRIORITY_MAX)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).also {
                it.description = channelDsc
                it.setShowBadge(true)
                it.canShowBadge()
                it.enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channelId)
        }

        try {
            when (message.data[fcmStyle]) {
                Style.BigPicture.name -> {
                    val picture = message.data[fcmPicture]
                    if (!picture.isNullOrEmpty()) {
                        val url = URL(picture)
                        val bigPicture =
                            BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        notificationBuilder.setLargeIcon(bigPicture)
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                                .setSummaryText(message.data[fcmText])
                        )
                    }
                }

                Style.BigText.name -> {
                    notificationBuilder.setStyle(
                        NotificationCompat.BigTextStyle()
                            .setBigContentTitle(message.data[fcmTitle])
                            .bigText(message.data[fcmText])
                    )
                }

                Style.Inbox.name -> {
                    val list = JSONArray(message.data["list"])
                    val style = NotificationCompat.InboxStyle()
                    for (i in 0 until list.length()) {
                        style.addLine(list.getString(i))
                    }
                    notificationBuilder.setStyle(style)
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

}