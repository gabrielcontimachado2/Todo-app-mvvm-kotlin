package com.bootcamp.todoeasy.util.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.ui.activitys.MainActivity
import com.bootcamp.todoeasy.util.date.FormatDate

class TaskNotificationService(
    private val context: Context,
    private val task: Task,
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /** Function to show the notification when alarm receiver call him */
    fun showNotification() {

        val intent = Intent(context, MainActivity::class.java)
        //val taskBundle = Bundle()
        //taskBundle.putParcelable("task", task)
        //intent.putExtra("task", taskBundle)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT
        )

        val format = FormatDate()
        val formartDateToString = format.formatDate(task.date)

        val finish = context.getString(R.string.finish, task.name, formartDateToString)

        val notification = NotificationCompat.Builder(context, TASK_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle(task.name)
            .setContentText(finish)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val TASK_CHANNEL_ID = "task_channel"
    }
}