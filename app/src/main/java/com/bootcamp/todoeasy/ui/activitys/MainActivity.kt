package com.bootcamp.todoeasy.ui.activitys


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityMainBinding
import com.bootcamp.todoeasy.ui.activitys.detailCategory.DetailCategoryActivity
import com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory.CategoryDialogFragment
import com.bootcamp.todoeasy.ui.fragments.today.TaskViewModel
import com.bootcamp.todoeasy.util.onQueryTextChanged
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private var listFilter: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomBar()
        setupFabButton()
        setupChipGroup()
        setupCreateCategory()

    }

    /** Function for menu item clicked in bottom bar menu*/
    private fun setupBottomBar() {
        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.category_detail -> {
                    openDetailCategory()
                    true
                }
                else -> true
            }

            /**Don't work ;( TODO*/
            //val searchQuery = it.subMenu.findItem(R.id.action_search)
            //val searchView = searchQuery.actionView as SearchView

            //searchView.onQueryTextChanged { queryChanged ->
            //    viewModel.searchTask.value = queryChanged
            //}

            //true
        }


    }


    /** Open the Dialog Fragment for create category*/
    private fun setupCreateCategory() {
        val createCategory = binding.categoryFilter.addCategory

        createCategory.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }


    /** Create the Chip Group with Category from Room */
    private fun setupChipGroup() {

        val categoryChipGroup = binding.categoryFilter.chipGroupCategory

        viewModel.category.observe(this) { categoryList ->
            categoryList.forEach { category ->
                if (!listFilter.contains(category.categoryName)) {
                    listFilter.add(category.categoryName)
                }
            }

            with(categoryChipGroup) {
                removeAllViews()
                listFilter.forEach { categoryString ->
                    val chip = layoutInflater.inflate(R.layout.single_chip, null) as Chip
                    chip.text = categoryString
                    addView(chip)

                    /** Check which Chip was Selected */
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.setCategoryFilter(chip.text.toString())
                            viewModel.updateTaskWithCategory()
                        }
                    }
                }
            }
        }
    }


    /** Open the Modal Bottom Sheet for create the Task */
    private fun setupFabButton() {
        val fabCreateTask = binding.floatingButtonCreateTask

        fabCreateTask.setOnClickListener {

            val navController =
                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.modalBottomSheet)
        }
    }


    /** Open the Detail Category Activity */
    private fun openDetailCategory() {
        val intent = Intent(this, DetailCategoryActivity::class.java)
        startActivity(intent)
    }


}