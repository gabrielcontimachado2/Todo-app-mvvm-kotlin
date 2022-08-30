package com.bootcamp.todoeasy.ui.fragments.modal

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.databinding.ModalBottomSheetBinding
import com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory.CategoryDialogFragment
import com.bootcamp.todoeasy.util.*
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_MEDIUM
import com.bootcamp.todoeasy.util.alarm.SetAlarm
import com.bootcamp.todoeasy.util.date.FormatDate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId
import java.util.*


@AndroidEntryPoint
class ModalBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: ModalBottomSheetBinding
    private val viewModel: ModalViewModel by viewModels()

    private var name: String = ""
    private var description: String = ""
    private var priority: Int = 1
    private var category: String = ""
    private var date: Long = 0
    private var time: String = ""
    private var alarmHour: Int = 0
    private var alarmMinute: Int = 0
    private var createdTime: Long = 0
    private lateinit var fieldEmpty: String
    //private var alarmYear: Int = 0
    //private var alarmMonth: Int = 0
    //private var alarmDay: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ModalBottomSheetBinding.inflate(inflater, container, false)

        setupShowDatePicker()
        setupShowTimePicker()
        radioGroupPriority()
        // setupAutoCompleteCategory()
        setupCreateCategory()
        setupCategoryChip()
        actionEnter()
        addTask()

        return binding.root
    }


    /** Open the Dialog Fragment for create category
     * In that function when button create category clicked
     * i created one dialog for Category Dialog Fragment and show him in screen*/
    private fun setupCreateCategory() {
        val createCategory = binding.categoryFilterLayout.addCategory

        createCategory.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }
    }

    /** Function to create the chip group and children chips
     *  In that function i observer the category in viewModel with list of category
     *  and when categories values change, i remove all children chips in chip group,
     *  and create the new children with new list of categories, and before the forEach i created one chip for all categories*/
    private fun setupCategoryChip() {
        val categoryChipGroup = binding.categoryFilterLayout.chipGroupCategory

        /** Observer the Livedata With Categories */
        viewModel.category.observe(this) { categoryList ->

            with(categoryChipGroup) {
                removeAllViews()

                //ForEach to read the categoryList and create the chips without repeat values
                categoryList.forEach { categoryForEach ->

                    val chip = layoutInflater.inflate(R.layout.single_chip, null) as Chip
                    chip.text = categoryForEach.categoryName
                    addView(chip)

                    /** Check which Chip was Selected */
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            category = chip.text.toString()
                        }
                    }
                }
            }
        }
    }

    /** Enter Action in the AutoCompleteText*/
    private fun actionEnter() {
        binding.ediTextDescriptionTask.setOnEnterKeyListener(clickedAction())
    }

    /** Action When Click */
    private fun clickedAction(): () -> Unit {
        val onClickAction = {
            hideKeyboard()
        }
        return onClickAction
    }

    /** Function to create a task and his alarm
     *  In that function i created the task in room db for that
     *  first i check in the function "checkFieldNotEmpty" if we have fields empty
     *  and start create the task, create his id with the randomUUID and
     *  i created the variable for task and pass him for viewModel, in a function to create a task,
     *  last i created a alarm with "setAlarm", and show message for completed operation*/
    private fun addTask() {

        binding.imageButtonAddTask.setOnClickListener {

            if (checkFieldNotEmpty()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createdTime = Calendar.getInstance().timeInMillis.toUTCLocalDateTime()
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }

                /** Random ID for Task*/
                val taskId = UUID.randomUUID().toString()

                val task =
                    Task(
                        taskId,
                        name,
                        description,
                        false,
                        priority,
                        category,
                        Date(date),
                        time,
                        Date(createdTime),
                        true,
                        ""
                    )

                viewModel.insertTask(task)

                setAlarm(
                    requireContext(),
                    task,
                    alarmHour,
                    alarmMinute,
                    date,
                    createdTime
                )


                clearFields()
                disableErrorMessage()
                Snackbar.make(
                    dialog!!.window!!.decorView,
                    R.string.create_task_completed,
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            } else {
                Snackbar.make(
                    dialog!!.window!!.decorView,
                    getString(R.string.error_empty_field, fieldEmpty),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    /** Function to Set the Alarm
     *  in that function first i instantiating the setAlarm class, and pass the parameters he needed to create a alarm*/
    private fun setAlarm(
        context: Context,
        task: Task,
        alarmHour: Int,
        alarmMinute: Int,
        date: Long,
        createdTime: Long
    ) {
        val setAlarm = SetAlarm(context)

        setAlarm.setAlarmTask(task, alarmHour, alarmMinute, date, createdTime, interval = "")
    }


    /** Check if the Inputs is Empty
     *  I check all field before created a task, to see if one of them was empty*/
    private fun checkFieldNotEmpty(): Boolean {

        /** Get the values in fields for test if is empty */
        with(binding) {
            name = ediTextNameTask.text.toString().replaceFirstChar { it.uppercase() }
            description = ediTextDescriptionTask.text.toString().replaceFirstChar { it.uppercase() }
        }

        when {
            name.isEmpty() -> {
                binding.textInputNameTask.isErrorEnabled = true
                binding.textInputNameTask.error = getString(R.string.error_text)

                fieldEmpty = getString(R.string.name)
                return false
            }
            description.isEmpty() -> {
                binding.textInputDescriptionTask.isErrorEnabled = true
                binding.textInputDescriptionTask.error = getString(R.string.error_text)

                fieldEmpty = getString(R.string.description)
                return false
            }
            category.isEmpty() -> {
                //binding.textInputCategoryTask.isErrorEnabled = true
                //binding.textInputCategoryTask.error = getString(R.string.error_text)

                fieldEmpty = getString(R.string.category)

                return false
            }
            time.isEmpty() -> {

                fieldEmpty = getString(R.string.time)
                return false
            }
            date.equals(null) -> {

                fieldEmpty = getString(R.string.time)
                return false
            }
            else -> {
                disableErrorMessage()
                return true
            }
        }
    }

    /** Logic for Change action in RadioGroup */
    private fun radioGroupPriority() {
        val radioGroup = binding.radioGroupPriority

        radioGroup.setOnCheckedChangeListener { _, id ->
            priority = when (id) {
                R.id.radioButton_high -> PRIORITY_TASK_HIGH
                R.id.radioButton_medium -> PRIORITY_TASK_MEDIUM
                R.id.radioButton_low -> PRIORITY_TASK_LOW
                else -> PRIORITY_TASK_LOW
            }
        }
    }

    /** Clear Inputs after task created */
    private fun clearFields() {
        with(binding) {
            ediTextNameTask.text?.clear()
            ediTextDescriptionTask.text?.clear()
            //autoCompleteCategory.text?.clear()
            textViewHourUpdate.text = ""
            textViewDateUpdate.text = ""
            radioGroupPriority.clearCheck()
        }
    }

    /** Hide error messages */
    private fun disableErrorMessage() {
        with(binding) {
            textInputNameTask.isErrorEnabled = false
            textInputNameTask.error = null
            textInputDescriptionTask.isErrorEnabled = false
            textInputDescriptionTask.error = null
            //textInputCategoryTask.isErrorEnabled = false
            //textInputCategoryTask.error = null
        }
    }

    /** Open the Material Time Picker and set Hour*/
    private fun setupShowTimePicker() {
        val hour = binding.imageButtonHour

        hour.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Task Time")
                    .build()

            picker.show(childFragmentManager, "TimePicker")

            picker.addOnPositiveButtonClickListener {

                /** String Format */
                time = String.format("%02d:%02d", picker.hour, picker.minute)

                /** Get the Pick Hour and Minute */
                alarmHour = picker.hour
                alarmMinute = picker.minute
                binding.textViewHourUpdate.text = time
            }

        }
    }

    /** Open the Material Date Picker, set and Convert Date */
    private fun setupShowDatePicker() {
        val calendar = binding.imageButtonDate

        calendar.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Task Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(childFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {

                /** Format the Date*/
                val format = FormatDate()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date = it.toUTCLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()
                }

                binding.textViewDateUpdate.text = format.formatLong(date)
            }
        }

    }

}
