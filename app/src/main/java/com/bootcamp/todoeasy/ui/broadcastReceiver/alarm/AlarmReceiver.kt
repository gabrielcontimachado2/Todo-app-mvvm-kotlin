package com.bootcamp.todoeasy.ui.broadcastReceiver.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.util.notification.TaskNotificationService


class AlarmReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val taskBundle = intent.getBundleExtra("task")
        val task = taskBundle?.getParcelable<Task>("task")

        val notificationService = TaskNotificationService(context, task!!)

        notificationService.showNotification()
    }
}

