package com.bootcamp.todoeasy.ui.activitys.detailTask

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.databinding.ActivityDetailTaskBinding
import com.bootcamp.todoeasy.ui.fragments.category.dialogUpdateCategory.CategoryUpdateDialogFragment
import com.bootcamp.todoeasy.util.Constants.Companion.DAY
import com.bootcamp.todoeasy.util.Constants.Companion.MONTH
import com.bootcamp.todoeasy.util.Constants.Companion.WEEKLY
import com.bootcamp.todoeasy.util.date.FormatDate
import com.bootcamp.todoeasy.util.toUTCLocalDateTime
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId


@AndroidEntryPoint
class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding
    private val args: DetailTaskActivityArgs by navArgs()
    private val viewModel: DetailTaskViewModel by viewModels()
    private var listCategory = mutableListOf<String>()
    private lateinit var taskArg: Task
    private lateinit var taskId: String
    private lateinit var taskDescription: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupToolbar()
        observerTask()
        setupExposedDownCategory()
        setupDueDate()
        setupTime()
    }

    /** Setup Hour and update the task with new Hour */
    private fun setupTime() {
        binding.hour.cardHour.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Task Time")
                    .build()

            picker.show(supportFragmentManager, "TimePicker")

            picker.addOnPositiveButtonClickListener {

                val time = String.format("%02d:%02d", picker.hour, picker.minute)

                viewModel.updateHour(taskId, time)
                viewModel.updateTask(taskId)
            }

        }

    }

    /** Setup Due Date and update the task with new Due Date */
    private fun setupDueDate() {
        binding.dueDate.cardDueDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Task Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {

                val date = it.toUTCLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()

                viewModel.updateDueDate(taskId, date)
                viewModel.updateTask(taskId)
            }
        }

    }

    /** Exposed Popup Menu When clicked in category Text Field */
    private fun setupExposedDownCategory() {

        binding.textViewCategory.setOnClickListener { view ->
            textViewPopupMenu(view)
        }
    }

    /** Create a Popup Menu for Category*/
    private fun textViewPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.menu_category, popupMenu.menu)

        viewModel.category.observe(this) { categoryList ->
            categoryList.forEach { category ->
                if (!listCategory.contains(category.categoryName)) {
                    listCategory.add(category.categoryName)
                }
            }

        }

        listCategory.forEach { category ->
            popupMenu.menu.add(category)
        }

        popupMenu.setOnMenuItemClickListener { item ->

            if (item.toString() == getString(R.string.create_category)) {

                val bundle = Bundle()
                bundle.putString("taskId", taskId)

                val dialog = CategoryUpdateDialogFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, dialog.tag)

                dialog.onDismissListener = {
                    viewModel.updateTask(taskId)
                }

            } else {

                viewModel.updateCategory(taskId, item.toString())
                viewModel.updateTask(taskId)
            }

            true
        }

        popupMenu.show()
    }

    /**Observer the Task and set values in Detail Screen*/
    private fun observerTask() {

        taskId = args.task.idTask

        viewModel.updateTask(taskId)

        viewModel.task.observe(this) { task ->
            val descriptionEditable = getEditable(task.description)
            val titleEditable = getEditable(task.name)
            val formatDate = FormatDate()

            binding.ediTextCategoryDescription.text = descriptionEditable
            binding.ediTextTitle.text = titleEditable
            binding.dueDate.textViewDrawableDate.text = formatDate.formatDate(task.date)
            binding.hour.textViewDrawableHour.text = task.hour
            binding.textViewCategory.text = task.categoryName

            if (task.reminder) {
                binding.reminder.textViewDrawableReminder.text = getString(R.string.yes)
            } else {
                binding.reminder.textViewDrawableReminder.text = getString(R.string.no)
            }

            when (task.interval) {
                DAY -> {
                    binding.repeat.textViewDrawableRepeat.text = DAY
                }
                WEEKLY -> {
                    binding.repeat.textViewDrawableRepeat.text = WEEKLY
                }
                MONTH -> {
                    binding.repeat.textViewDrawableRepeat.text = MONTH
                }
                else -> {
                    binding.repeat.textViewDrawableRepeat.text = getString(R.string.no)
                }

            }

        }

    }

    private fun getEditable(string: String) = Editable.Factory.getInstance().newEditable(string)

    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                true
            }
            R.id.complete_task -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}