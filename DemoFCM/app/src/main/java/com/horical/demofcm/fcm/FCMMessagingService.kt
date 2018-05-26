package com.horical.demofcm.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.horical.demofcm.MainActivity
import com.horical.demofcm.MyJobService
import com.horical.demofcm.R
import com.horical.demofcm.extension.tag

/**
 * Created by Nhơn Võ on 5/26/18
 */
class FCMMessagingService : FirebaseMessagingService() {

    companion object {
        val TAG: String = FCMMessagingService::class.java.simpleName.tag()
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            scheduleJob()
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message notification body: " + remoteMessage.notification?.body)
        }

        sendNotification(remoteMessage.notification?.body)
    }

    private fun sendNotification(messageText: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT)
        val chanelId = getString(R.string.default_notification_chanel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationCompat = NotificationCompat.Builder(this, chanelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FCM Message DEMO")
                .setContentText(messageText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel: NotificationChannel = NotificationChannel(
                    chanelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(chanel)
        }

        notificationManager.notify(0, notificationCompat.build())
    }

    private fun scheduleJob() {
        val dispatcher: FirebaseJobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val job = dispatcher.newJobBuilder()
                .setService(MyJobService::class.java)
                .setTag("my-job-tag")
                .build()
        dispatcher.schedule(job)
    }
}