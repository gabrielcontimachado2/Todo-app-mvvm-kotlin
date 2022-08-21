package com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category

import com.bootcamp.todoeasy.databinding.DialogCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDialogFragment : DialogFragment() {

    private lateinit var binding: DialogCategoryBinding
    private val viewModel: CategoryDialogViewModel by viewModels()

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
            getDrawable(
                requireContext(),
                R.drawable.button_category
            )
        )
    }

    /** Function to Create category */
    private fun addCategory() {
        binding.btnCreateCategory.setOnClickListener {
            if (checkFieldNotEmpty()) {
                val newCategory = Category(
                    null,
                    binding.ediTextNameCategory.text.toString().replaceFirstChar { it.uppercase() })//uppercase first letter

                viewModel.insertCategory(newCategory)

                dismiss()

            }
        }
    }

    /** Function for Check if the fields is not empty */
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