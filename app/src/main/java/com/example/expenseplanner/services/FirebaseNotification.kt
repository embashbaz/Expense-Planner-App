package com.example.expenseplanner.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Repository
import com.example.expenseplanner.ui.map.MapViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.HashMap

class PushNotificationService : FirebaseMessagingService() {

    val repository = Repository()

    override fun onNewToken(s: String) {
        super.onNewToken(s)


            val sharedPref = application.getSharedPreferences("my_data", Context.MODE_PRIVATE)?: return
                with(sharedPref.edit()){
                    putString("msg_token", s)
                    apply()
                }



        //val tokenData: MutableMap<String, Any> = HashMap()
       // tokenData["token"] = s
      //  val firestore = FirebaseFirestore.getInstance()
      //  firestore.collection("DeviceTokens").document().set(tokenData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body
        val CHANNEL_ID = "HEADS_UP_NOTIFICATIONS"
        val channel = NotificationChannel(
            CHANNEL_ID,
            "MyNotification",
            NotificationManager.IMPORTANCE_HIGH
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_shopping_cart_24)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(1, notification.build())
    }
}