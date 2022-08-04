package com.bootcamp.todoeasy.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        //val taskBundle = intent!!.getByteArrayExtra("task")
        //val intent = Intent(context, DetailTaskActivity::class.java)
        //intent.putExtra("task", task)

        val notificationService = TaskNotificationService(context)

        notificationService.showNotification()
    }
}

