package com.bootcamp.todoeasy.ui.activitys.detailCategory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityDetailCategoryBinding
import com.bootcamp.todoeasy.databinding.ActivityDetailTaskBinding
import com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory.CategoryDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCategoryBinding
    private val viewModel: DetailCategoryViewModel by viewModels()
    private var listFilter: MutableSet<String> = mutableSetOf()
    private var categorySelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupChipGroup()
        deleteCategory()
        createCategory()
    }

    /** Open the Dialog Fragment for Create Category */
    private fun createCategory() {

        binding.buttonAddCategory.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }

    }

    /** Delete the Category Selected */
    private fun deleteCategory() {
        binding.buttonDeleteCategory.setOnClickListener {
            if (checkFieldsNotEmpty()) {
                viewModel.deleteCategory(categorySelected)
                listFilter.remove(categorySelected)
            } else {
                Toast.makeText(this, R.string.category_empty, Toast.LENGTH_LONG).show()
            }
        }
    }

    /** Check if The Category Selected is Empty */
    private fun checkFieldsNotEmpty(): Boolean {
        return categorySelected.isNotEmpty()
    }


    /** Create the Chip Group with Category from Room */
    private fun setupChipGroup() {

        val categoryChipGroup = binding.layoutCategory.chipGroupCategory

        viewModel.category.observe(this) { categoryList ->
            categoryList.forEach { category ->
                if (!listFilter.contains(category.categoryName)) {
                    listFilter.add(category.categoryName)
                }
            }

            with(categoryChipGroup) {
                removeAllViews()
                listFilter.forEach { categoryString ->
                    val chip = layoutInflater.inflate(
                        R.layout.single_chip,
                        null
                    ) as Chip
                    chip.text = categoryString
                    addView(chip)

                    chip.setOnCheckedChangeListener { _, isChecked ->
                        categorySelected = if (isChecked) {
                            chip.text.toString()
                        } else {
                            ""
                        }
                    }
                }
            }
        }
    }

    /** Toolbar */
    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarDetailCategory)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}