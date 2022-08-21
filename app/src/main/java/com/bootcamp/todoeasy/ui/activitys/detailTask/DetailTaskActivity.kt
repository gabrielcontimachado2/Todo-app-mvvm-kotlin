package com.bootcamp.todoeasy.ui.activitys.detailTask

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.navArgs
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityDetailTaskBinding
import com.bootcamp.todoeasy.ui.fragments.category.dialogUpdateCategory.CategoryUpdateDialogFragment
import com.bootcamp.todoeasy.ui.fragments.priority.PriorityDialogFragment
import com.bootcamp.todoeasy.util.Constants.Companion.DAY
import com.bootcamp.todoeasy.util.Constants.Companion.HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.JOB_DELAY
import com.bootcamp.todoeasy.util.Constants.Companion.LOW
import com.bootcamp.todoeasy.util.Constants.Companion.MEDIUM
import com.bootcamp.todoeasy.util.Constants.Companion.MONTH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_MEDIUM
import com.bootcamp.todoeasy.util.Constants.Companion.WEEK
import com.bootcamp.todoeasy.util.date.FormatDate
import com.bootcamp.todoeasy.util.toUTCLocalDateTime
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.ZoneId


@AndroidEntryPoint
class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding
    private val args: DetailTaskActivityArgs by navArgs()
    private val viewModel: DetailTaskViewModel by viewModels()
    private var listCategory = mutableListOf<String>()
    private var status: Boolean = false
    private lateinit var taskId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupToolbar()
        observerTask()
        setupExposedDownCategory()
        setupDueDate()
        setupTime()
        setupTitleAndDescription()
        setupPriority()
    }

    /** Function for Open the Dialog for Update the Task Priority*/
    private fun setupPriority() {

        binding.priority.cardPriority.setOnClickListener {

            val dialogPriority = PriorityDialogFragment()

            val bundleTaskId = Bundle()
            bundleTaskId.putString("taskId", taskId)
            bundleTaskId.putInt("taskPriority", args.task.priority)

            dialogPriority.arguments = bundleTaskId
            dialogPriority.show(supportFragmentManager, "priorityDialog")

            dialogPriority.onDismissListener = {
                viewModel.updateTask(taskId)
            }

        }
    }

    /** Function to Update the Title and Description when Text Changed, in two Jobs with a Delay the 0.5 Seconds */
    private fun setupTitleAndDescription() {

        val scope = CoroutineScope(Job() + Dispatchers.IO)

        val jobTitleAndDescription = scope.launch {
            delay(JOB_DELAY)

            val jobTitle = launch {
                binding.ediTextTitle.addTextChangedListener { titleEditable ->
                    if (titleEditable.toString().isNotEmpty()) {
                        viewModel.updateTitle(taskId, titleEditable.toString())
                    }
                }
            }

            val jobDescription = launch {
                binding.ediTextCategoryDescription.addTextChangedListener { descriptionEditable ->
                    if (descriptionEditable.toString().isNotEmpty()) {
                        viewModel.updateDescription(taskId, descriptionEditable.toString())
                    }
                }
            }

        }

    }

    /** Function to Setup Hour and update the task with new Hour */
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
                binding.hour.textViewDrawableHour.text = time
                //viewModel.updateTask(taskId)
            }

        }

    }

    /** Function to Setup Due Date and update the task with new Due Date */
    private fun setupDueDate() {
        binding.dueDate.cardDueDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Task Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {

                val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.toUTCLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                viewModel.updateDueDate(taskId, date)

                val formart = FormatDate()
                binding.dueDate.textViewDrawableDate.text = formart.formatLong(date)
                //viewModel.updateTask(taskId)
            }
        }

    }

    /** Function for Exposed Popup Menu When clicked in category Text Field */
    private fun setupExposedDownCategory() {

        binding.textViewCategory.setOnClickListener { view ->
            textViewPopupMenu(view)
        }
    }

    /** Function to Create a Popup Menu for Category*/
    private fun textViewPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.menu_category, popupMenu.menu)

        /** Observer the Category LiveData for create a List with the categories */
        viewModel.category.observe(this) { categoryList ->
            categoryList.forEach { category ->

                /** Create a List without Items Repeated */
                if (!listCategory.contains(category.categoryName)) {
                    listCategory.add(category.categoryName)
                }
            }

        }

        /** Add the categories in PopupMenu */
        listCategory.forEach { category ->
            popupMenu.menu.add(category)
        }

        popupMenu.setOnMenuItemClickListener { item ->

            /** Open the Create Category Dialog When "Create New" was Selected and Update the Value with that */
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

    /**Function to Observer the Task and set values in Detail Screen*/
    private fun observerTask() {

        /** Get the TaskId the Task in the Arguments */
        taskId = args.task.idTask

        viewModel.updateTask(taskId)

        /** Observer the task and setup All inputs With the values update in task */
        viewModel.task.observe(this) { task ->

            val descriptionEditable = getEditable(task.description)
            val titleEditable = getEditable(task.name)
            val formatDate = FormatDate()

            binding.ediTextCategoryDescription.text = descriptionEditable
            binding.ediTextTitle.text = titleEditable
            binding.dueDate.textViewDrawableDate.text = formatDate.formatDate(task.date)
            binding.hour.textViewDrawableHour.text = task.hour
            binding.textViewCategory.text = task.categoryName
            status = task.status

            if (task.reminder) {
                binding.reminder.textViewDrawableReminder.text = getString(R.string.yes)
            } else {
                binding.reminder.textViewDrawableReminder.text = getString(R.string.no)
            }

            when (task.interval) {
                DAY -> {
                    binding.repeat.textViewDrawableRepeat.text = DAY
                }
                WEEK -> {
                    binding.repeat.textViewDrawableRepeat.text = WEEK
                }
                MONTH -> {
                    binding.repeat.textViewDrawableRepeat.text = MONTH
                }
                else -> {
                    binding.repeat.textViewDrawableRepeat.text = getString(R.string.no)
                }

            }

            when (task.priority) {
                PRIORITY_TASK_LOW -> {
                    binding.priority.textViewDrawablePriority.text = LOW
                }
                PRIORITY_TASK_MEDIUM -> {
                    binding.priority.textViewDrawablePriority.text = MEDIUM
                }
                PRIORITY_TASK_HIGH -> {
                    binding.priority.textViewDrawablePriority.text = HIGH
                }
                else -> {
                    binding.priority.textViewDrawablePriority.text = LOW
                }

            }

        }

    }

    /** Function for Convert String to Editable for Set Text in EditText */
    private fun getEditable(string: String) = Editable.Factory.getInstance().newEditable(string)

    /** Toolbar */
    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_top_bar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                showDialogDelete()
                true
            }
            R.id.complete_task -> {
                showDialogComplete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Function to Build and Show the Alert Dialog for Complete or Undone the task */
    private fun showDialogComplete() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.complete_task))

        if (status) {
            alertDialogBuilder.setMessage(getString(R.string.message_complete_undone))
        } else {
            alertDialogBuilder.setMessage(getString(R.string.message_complete))
        }

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            status = if (status) {
                viewModel.updateStatus(taskId, false)
                Toast.makeText(this, R.string.complete_task_message_undo, Toast.LENGTH_LONG).show()
                false
            } else {
                viewModel.updateStatus(taskId, true)
                Toast.makeText(this, R.string.complete_task_message, Toast.LENGTH_LONG).show()
                true
            }
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }

    /** Function to Build and Show the Alert Dialog for Delete the task and if user Want delete Finish this Activity */
    private fun showDialogDelete() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.delete_task))
        alertDialogBuilder.setMessage(getString(R.string.message_delete))

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            viewModel.deleteTask(args.task)
            finish()
            Toast.makeText(this, R.string.task_deleted_success, Toast.LENGTH_LONG).show()

        }

        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }


}