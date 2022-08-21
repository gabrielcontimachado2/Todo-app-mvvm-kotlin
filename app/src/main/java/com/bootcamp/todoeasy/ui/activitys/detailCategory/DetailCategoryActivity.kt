package com.bootcamp.todoeasy.ui.activitys.detailCategory

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.databinding.ActivityDetailCategoryBinding
import com.bootcamp.todoeasy.ui.adapter.CategoryAdapter
import com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory.CategoryDialogFragment
import com.bootcamp.todoeasy.ui.fragments.category.dialogEditCategory.CategoryEditDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCategoryBinding
    private val viewModel: DetailCategoryViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupOptionsEdit()
        observerCategory()
        createCategory()
    }

    /** Create the popupMenu for Edit or Delete the category */
    private fun setupOptionsEdit() {
        categoryAdapter.setonCheckClickListener { category, view ->

            val popupMenu = PopupMenu(this, view)

            popupMenu.menuInflater.inflate(R.menu.menu_edit_category, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->

                /** Open the Edit Category Dialog and Update or Delete Category selected */
                when (item.itemId) {
                    R.id.item_edit_category -> {
                        val bundle = Bundle()
                        bundle.putParcelable("category", category)

                        val dialog = CategoryEditDialogFragment()
                        dialog.arguments = bundle
                        dialog.show(supportFragmentManager, dialog.tag)

                    }
                    R.id.item_delete_category -> {
                        showDialogDelete(category)
                    }
                }
                true
            }

            popupMenu.show()
        }
    }

    /** Observer the Ui StateFlow and get the Latest Category Emitted */
    private fun observerCategory() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiStateCategory.collect { categoryUiState ->
                    categoryAdapter.submitList(categoryUiState.categoryWithTask)
                }
            }
        }
    }

    /** Setup the Recycler View with the Categories */
    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter()
        recyclerView = binding.recyclerViewCategories

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    /** Open the Dialog Fragment for Create Category */
    private fun createCategory() {

        binding.buttonAddCategory.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }

    }

    /** Function to Build and Show the Alert Dialog for Delete the category */
    private fun showDialogDelete(category: Category) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.delete_task))
        alertDialogBuilder.setMessage(getString(R.string.message_delete_category))

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            viewModel.deleteCategory(category)
            Toast.makeText(this, R.string.category_deleted_success, Toast.LENGTH_LONG).show()

        }

        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }


    /** Toolbar */
    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarDetailCategory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}