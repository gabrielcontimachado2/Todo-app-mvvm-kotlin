package com.bootcamp.todoeasy.ui.fragments.category.dialogEditCategory

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.databinding.DialogCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryEditDialogFragment : DialogFragment() {

    private lateinit var binding: DialogCategoryBinding
    private val viewModel: CategoryEditViewModel by viewModels()
    private val categoryArgs: CategoryEditDialogFragmentArgs by navArgs()
    lateinit var onDismissListener: () -> Any

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCategoryBinding.inflate(inflater, container, false)

        setCategoryName()
        editCategory()
        cancelDialog()

        return binding.root
    }

    /** Function to set the current Name when Dialog Fragment was Created */
    private fun setCategoryName() {
        binding.ediTextNameCategory.text = getEditable(categoryArgs.category.categoryName)
    }

    /** Function for Convert String to Editable for Set Text in EditText */
    private fun getEditable(string: String) = Editable.Factory.getInstance().newEditable(string)

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

    /** Function to Edit the Category */
    private fun editCategory() {
        binding.btnCreateCategory.setOnClickListener {
            if (checkFieldNotEmpty()) {

                viewModel.editCategory(
                    categoryArgs.category.idCategory!!,
                    binding.ediTextNameCategory.text.toString().replaceFirstChar { it.uppercase() }
                )

                dismiss()
            }
        }
    }

    /** Function for check if the fields is not Empty */
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