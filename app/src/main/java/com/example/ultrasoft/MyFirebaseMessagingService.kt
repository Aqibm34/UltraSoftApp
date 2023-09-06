package com.example.ultrasoft

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.logE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
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
    private val channelId = "YM Channel"

    private enum class Style { BigText, BigPicture, Inbox }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        AppConstants.FCM_TOKEN_REFRESHED = token
        logE("On Token Refreshed", token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            logE("FCM Payload", message.data.toString())
        }
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

//        val bundle = Bundle()
//        bundle.putString(fcmParam, data[fcmParam])
//        val intent = Intent(this, WelcomeScreenActivity::class.java)
//        intent.putExtras(bundle)
//        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.FLAG_MUTABLE
//        } else {
//            PendingIntent.FLAG_ONE_SHOT
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
//            flag)

//        val resourceId = R.raw.noti
//        val soundTrackUri = Uri.Builder()
//            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//            .authority(resources.getResourcePackageName(resourceId))
//            .appendPath(resources.getResourceTypeName(resourceId))
//            .appendPath(resources.getResourceEntryName(resourceId))
//            .build()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.data[fcmTitle])
            .setContentText(message.data[fcmText])
            .setAutoCancel(true)
//            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//            .setSound(soundTrackUri)
            .setNumber(numMessages)
//          .setContentIntent(pendingIntent)
//          .setContentInfo("YourMudra")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
//          .setColor(getColor(R.color.blue))
//          .setLights(Color.RED, 1000, 300)
//          .setDefaults(Notification.DEFAULT_VIBRATE)
//          .setSmallIcon(R.drawable.del_icon)

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
//            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                val audioAttribute = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
//                it.setSound(soundTrackUri, audioAttribute)
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

            notificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channelId)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

}