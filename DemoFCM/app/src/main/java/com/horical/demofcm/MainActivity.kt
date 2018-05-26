package com.horical.demofcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.horical.demofcm.extension.tag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName.tag()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val chanelId = getString(R.string.default_notification_chanel_id)
            val chanelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel(
                    chanelId,
                    chanelName,
                    NotificationManager.IMPORTANCE_LOW))
        }

        if (intent.extras != null) {
            for (key in intent.extras.keySet()) {
                val objects = intent.extras.get(key)
                Log.d(TAG, "Key: $key Value: $objects")
            }
        }

        btnSub.setOnClickListener({
            Log.d(TAG, "Subscribing to news topic")
            FirebaseMessaging.getInstance().subscribeToTopic("news").addOnCompleteListener {
                var msg = getString(R.string.msg_subscriber)
                if (!it.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        })

        btnLogToken.setOnClickListener {
            val token = FirebaseInstanceId.getInstance().token
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }
}
