package com.bootcamp.todoeasy.ui.fragments.priority

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.DialogPriorityBinding
import com.bootcamp.todoeasy.util.Constants.Companion.HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.LOW
import com.bootcamp.todoeasy.util.Constants.Companion.MEDIUM
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_HIGH
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_LOW
import com.bootcamp.todoeasy.util.Constants.Companion.PRIORITY_TASK_MEDIUM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PriorityDialogFragment : DialogFragment() {

    private lateinit var binding: DialogPriorityBinding
    private val viewModel: PriorityViewModel by viewModels()
    private val taskArgs: PriorityDialogFragmentArgs by navArgs()
    lateinit var onDismissListener: () -> Any

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogPriorityBinding.inflate(inflater, container, false)

        setupCurrentPriority()
        setupAutoComplete()
        updatePriority()
        cancelDialog()

        return binding.root
    }

    /** Function for Convert String to Editable for Set Text in EditText */
    private fun getEditable(string: String) = Editable.Factory.getInstance().newEditable(string)


    /** Start the AutoComplete with the Current Priority Value */
    private fun setupCurrentPriority() {
        when (taskArgs.taskPriority) {

            PRIORITY_TASK_LOW -> {
                binding.autoCompletePriority.text = getEditable(LOW)
            }
            PRIORITY_TASK_MEDIUM -> {
                binding.autoCompletePriority.text = getEditable(MEDIUM)
            }
            PRIORITY_TASK_HIGH -> {
                binding.autoCompletePriority.text = getEditable(HIGH)
            }
            else -> {
                binding.autoCompletePriority.text = getEditable(LOW)
            }
        }
    }

    /** Function to create the autocomplete with his Array Adapter */
    private fun setupAutoComplete() {
        val priority = resources.getStringArray(R.array.priorityArray)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_category, priority)
        binding.autoCompletePriority.setAdapter(arrayAdapter)
    }

    private fun cancelDialog() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.button_category
            )
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (this::onDismissListener.isInitialized) {
            onDismissListener()
        }
        super.onDismiss(dialog)
    }

    /** Function to update the task priority */
    private fun updatePriority() {
        binding.btnCreateCategory.setOnClickListener {
            if (checkFieldNotEmpty()) {

                val priority = when (binding.autoCompletePriority.text.toString()) {

                    LOW -> {
                        PRIORITY_TASK_LOW
                    }
                    MEDIUM -> {
                        PRIORITY_TASK_MEDIUM
                    }
                    HIGH -> {
                        PRIORITY_TASK_HIGH
                    }
                    else -> {
                        PRIORITY_TASK_LOW
                    }
                }

                viewModel.updateTaskPriority(
                    taskArgs.taskId,
                    priority
                )

                dismiss()

            } else {
                Toast.makeText(requireContext(), R.string.error_empty_field, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    /** Function to check if the fields is not empty */
    private fun checkFieldNotEmpty(): Boolean {
        return if (binding.autoCompletePriority.text!!.isEmpty()) {

            binding.autoCompletePriority.error = getString(R.string.error_text)

            false
        } else {
            true
        }
    }
}