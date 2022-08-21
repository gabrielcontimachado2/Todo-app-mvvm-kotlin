package com.bootcamp.todoeasy.util.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.ui.broadcastReceiver.alarm.AlarmReceiver
import java.util.*


class SetAlarm(
    private val context: Context
) {

    /** Function to set the alarm */
    fun setAlarmTask(
        task: Task,
        alarmHour: Int,
        alarmMinute: Int,
        date: Long,
        createdTime: Long,
        interval: String
    ) {

        /** Create the intent and a bundle with a task*/
        val intent = Intent(context, AlarmReceiver::class.java)
        val taskBundle = Bundle()
        taskBundle.putParcelable("task",task)

        intent.putExtra("task", taskBundle)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        /** Create the Pending intent */
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            createdTime.toInt(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT else PendingIntent.FLAG_ONE_SHOT
        )

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMinute)
        }

        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )

    }
}