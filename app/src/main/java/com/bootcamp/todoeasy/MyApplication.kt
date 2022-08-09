package com.bootcamp.todoeasy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.bootcamp.todoeasy.util.notification.TaskNotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    val CHANNEL_NAME = "Alarm Task"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

    }

    /**Notification Channel*/
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    TaskNotificationService.TASK_CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    lightColor = Color.GREEN
                    enableLights(true)
                }
            channel.description = "Task Alarm"

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}