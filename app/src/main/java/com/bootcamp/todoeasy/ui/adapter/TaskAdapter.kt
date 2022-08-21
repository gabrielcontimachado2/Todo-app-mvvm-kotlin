package com.bootcamp.todoeasy.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.databinding.CardTaskBinding
import com.bootcamp.todoeasy.util.Constants.Companion.COLOR_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.COLOR_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.COLOR_MEDIUM
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_MEDIUM
import com.bootcamp.todoeasy.util.diffUtil.DiffUtilTask
import com.bootcamp.todoeasy.util.date.FormatDate

class TaskAdapter :
    androidx.recyclerview.widget.ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffUtilTask()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding = CardTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private var onCardClickListener: ((Task) -> Unit)? = null

    fun setonCardClickListener(listener: (Task) -> Unit) {
        onCardClickListener = listener
    }

    private var onCheckClickListener: ((Task) -> Unit)? = null

    fun setonCheckClickListener(listener: (Task) -> Unit) {
        onCheckClickListener = listener
    }

    override fun getItemCount() = currentList.size

    inner class TaskViewHolder(private val binding: CardTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {

                val formatDate = FormatDate()

                checkBoxCardStatusTask.isChecked =
                    task.status
                textViewCardTittleTask.text = task.name
                textViewCardTittleTask.paint.isStrikeThruText = task.status
                textViewCardCategoryTask.text = task.categoryName
                textViewCardHourTask.text = task.hour
                textViewCardCalendarTask.text = formatDate.formatDate(task.date)
                when (task.priority) {
                    PRIORITY_TASK_HIGH -> {
                        priorityLine.setBackgroundColor(Color.parseColor(COLOR_HIGH))
                    }
                    PRIORITY_TASK_MEDIUM -> {
                        priorityLine.setBackgroundColor(Color.parseColor(COLOR_MEDIUM))
                    }
                    PRIORITY_TASK_LOW -> {
                        priorityLine.setBackgroundColor(Color.parseColor(COLOR_LOW))
                    }
                }

                checkBoxCardStatusTask.setOnClickListener {
                    onCheckClickListener?.let { it(task) }
                }

                itemView.setOnClickListener {
                    onCardClickListener?.let { it(task) }
                }
            }

        }
    }

}
