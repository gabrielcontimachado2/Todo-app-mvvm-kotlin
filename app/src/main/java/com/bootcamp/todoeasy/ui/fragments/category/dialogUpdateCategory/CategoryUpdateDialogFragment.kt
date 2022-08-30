package com.bootcamp.todoeasy.ui.fragments.category.dialogUpdateCategory

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.databinding.DialogCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryUpdateDialogFragment : DialogFragment() {

    private lateinit var binding: DialogCategoryBinding
    private val viewModel: CategoryUpdateViewModel by viewModels()
    private val taskArgs: CategoryUpdateDialogFragmentArgs by navArgs()
    lateinit var onDismissListener: () -> Any

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryBinding.inflate(inflater, container, false)

        addCategory()
        cancelDialog()

        return binding.root
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

    /** Function to Create the category and Update the Task with this new Category */
    private fun addCategory() {
        binding.btnCreateCategory.setOnClickListener {
            if (checkFieldNotEmpty()) {

                val newCategory = Category(
                    null,
                    binding.ediTextNameCategory.text.toString().replaceFirstChar { it.uppercase() })

                viewModel.insertCategory(newCategory)

                viewModel.updateTaskCategory(
                    taskArgs.taskId,
                    binding.ediTextNameCategory.text.toString().replaceFirstChar { it.uppercase() }
                )

                dismiss()

            }
        }
    }

    /** Function for Check if the Fields is not Empty */
    private fun checkFieldNotEmpty(): Boolean {
        return if (binding.ediTextNameCategory.text!!.isEmpty()) {
            binding.textInputNameCategory.isErrorEnabled = true
            binding.textInputNameCategory.error = getString(R.string.error_text)

            false
        } else {
            true
        }
    }


}