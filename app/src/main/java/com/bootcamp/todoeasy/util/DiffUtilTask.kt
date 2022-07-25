package com.bootcamp.todoeasy.util

import androidx.recyclerview.widget.DiffUtil
import com.bootcamp.todoeasy.data.models.Task

class DiffUtilTask : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem.idTask == newItem.idTask && oldItem.created == newItem.created

    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem
}
