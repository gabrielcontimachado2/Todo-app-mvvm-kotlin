package com.bootcamp.todoeasy.util.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.bootcamp.todoeasy.data.models.Task

class DiffUtilTask : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem.idTask == newItem.idTask && oldItem.created == newItem.created

    override fun areContentsTheSame(oldItem: Task, newItem: Task):Boolean {
        return oldItem == newItem
    }

}
