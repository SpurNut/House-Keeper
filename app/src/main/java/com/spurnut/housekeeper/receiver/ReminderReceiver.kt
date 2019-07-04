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
import com.spurnut.housekeeper.tasksscreen.TaskDetailActivity


class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val house = intent?.getStringExtra(context.getString(R.string.house_intent_extra))
        val taskTitle = intent?.getStringExtra(context.getString(R.string.task_title_intent_extra))
        val taskId = intent?.getIntExtra(context.getString(R.string.taskId), 0)

        createNotificationChannel(context)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, context.getString(R.string.channelId))
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                //example for large icon
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.task_reminder) + " " + house)
                .setContentText(taskTitle)
                .setOngoing(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
        val opening_intent = Intent(context, TaskDetailActivity::class.java)
        opening_intent.putExtra(context.getString(R.string.task_id_detail_activity), taskId)
        val pendingIntent = PendingIntent.getActivity(
                context,
                taskId!!,
                opening_intent,
                PendingIntent.FLAG_ONE_SHOT
        )
        // example for blinking LED
        builder.setLights(-0x48e3e4, 1000, 2000)
        builder.setSound(Uri.parse("android.resource://"
                + context.packageName
                + "/raw/hykenfreak__notification_chime.mp3"))
        builder.setContentIntent(pendingIntent)
        manager.notify(taskId, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = context.getString(R.string.channel_name)
            val name = context.getString(R.string.channel_name)
//            val descriptionText = context.getString(R.string.description_bla)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(context.getString(R.string.channelId), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}