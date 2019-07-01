package com.spurnut.housekeeper.receiver

import android.app.NotificationChannel
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.graphics.BitmapFactory
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.net.Uri
import android.os.Build
import com.spurnut.housekeeper.MainActivity
import com.spurnut.housekeeper.R


class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        //you might want to check what's inside the Intent
//        if (intent.getStringExtra("myAction") != null && intent.getStringExtra("myAction") == "notify") {

        createNotificationChannel(context)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context,"13")
                .setSmallIcon(R.drawable.ic_add_a_photo_black_24dp)
                //example for large icon
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("my title")
                .setContentText("my message")
                .setOngoing(false)
                .setPriority(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                i,
                PendingIntent.FLAG_ONE_SHOT
        )
        // example for blinking LED
        builder.setLights(-0x48e3e4, 1000, 2000)
        builder.setSound(Uri.parse("android.resource://" + context.packageName + "/raw/hykenfreak__notification_chime.mp3"))
        builder.setContentIntent(pendingIntent)
        manager.notify(12345, builder.build())
//        }

    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = context.getString(R.string.channel_name)
            val name = "bla"
//            val descriptionText = context.getString(R.string.description_bla)
            val descriptionText = "blabla"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("13", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}