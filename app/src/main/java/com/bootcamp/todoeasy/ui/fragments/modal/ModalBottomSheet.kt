package com.bootcamp.todoeasy.ui.fragments.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.databinding.ModalBottomSheetBinding
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_MEDIUM
import com.bootcamp.todoeasy.util.FormatDate
import com.bootcamp.todoeasy.util.hideKeyboard
import com.bootcamp.todoeasy.util.setOnEnterKeyListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ModalBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: ModalBottomSheetBinding
    private val viewModel: ModalViewModel by viewModels()

    private var name: String = ""
    private var description: String = ""
    private var priority: Int = 0
    private var category: String = ""
    private var date: Long = 0
    private var time: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ModalBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupShowDatePicker()
        setupShowTimePicker()
        radioGroupPriority()
        actionEnter()
        addTask()

    }

    /** Enter Action in the AutoCompleteText*/
    private fun actionEnter() {
        binding.autoCompleteCategory.setOnEnterKeyListener(clickedAction())
    }

    /** Action When Click */
    private fun clickedAction(): () -> Unit {
        val onClickAction = {
            hideKeyboard()
        }
        return onClickAction
    }

    /** Create a task */
    private fun addTask() {
        val imageButtonAdd = binding.imageButtonAddTask

        imageButtonAdd.setOnClickListener {

            if (checkFieldsEmpty()) {

                val createdTime = Calendar.getInstance().timeInMillis

                val task =
                    Task(null, name, description, false, priority, category, Date(date), time, Date(createdTime))

                val category =
                    Category(null, category)

                viewModel.insertTask(task, category)
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
                    R.string.error_empty_field,
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    /** Check if Inputs is Empty */
    private fun checkFieldsEmpty(): Boolean {

        with(binding) {
            name = ediTextNameTask.text.toString()
            description = ediTextDescriptionTask.text.toString()
            category = autoCompleteCategory.text.toString()
        }

        when {
            name.isEmpty() -> {
                binding.textInputNameTask.isErrorEnabled = true
                binding.textInputNameTask.error = getString(R.string.error_text)

                return false
            }
            description.isEmpty() -> {
                binding.textInputDescriptionTask.isErrorEnabled = true
                binding.textInputDescriptionTask.error = getString(R.string.error_text)

                return false
            }
            category.isEmpty() -> {
                binding.textInputCategoryTask.isErrorEnabled = true
                binding.textInputCategoryTask.error = getString(R.string.error_text)

                return false
            }
            time.isEmpty() -> {

                return false
            }
            date.equals(null) -> {
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
            autoCompleteCategory.text?.clear()
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
            textInputCategoryTask.isErrorEnabled = false
            textInputCategoryTask.error = null
        }
    }

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
                time = "${picker.hour}:${picker.minute}"
                binding.textViewHourUpdate.text = time
            }

        }
    }

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
                date = it

                val format = FormatDate()
                binding.textViewDateUpdate.text = format.formatLong(date)
            }
        }
    }
}