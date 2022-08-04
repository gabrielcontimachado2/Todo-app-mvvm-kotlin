package com.bootcamp.todoeasy.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.ui.activitys.MainActivity

class TaskNotificationService(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, TASK_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle("Task Alarm")
            .setContentText("Task due date (falta implementar)")
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val TASK_CHANNEL_ID = "task_channel"
    }
}