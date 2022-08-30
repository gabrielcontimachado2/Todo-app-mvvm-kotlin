package com.bootcamp.todoeasy.ui.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityMainBinding
import com.bootcamp.todoeasy.ui.activitys.detailCategory.DetailCategoryActivity
import com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory.CategoryDialogFragment
import com.bootcamp.todoeasy.ui.fragments.today.TaskViewModel
import com.bootcamp.todoeasy.util.Constants.Companion.JOB_DELAY
import com.bootcamp.todoeasy.util.onQueryTextChanged
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private var hide: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeState()
        setupBottomBar()
        setupFabButton()
        setupChipGroup()
        setupCreateCategory()

    }

    /** Function to Observer the Ui State and if the Hide is active or not */
    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiStateMain.collect {
                hide = if (it.hideCompleted) {
                    it.hideCompleted
                } else {
                    it.hideCompleted
                }
            }
        }
    }

    /** Function for menu item clicked in bottom bar menu*/
    private fun setupBottomBar() {
        val searchQuery = binding.bottomAppBar.menu.findItem(R.id.action_search)
        val searchView = searchQuery.actionView as SearchView

        var jobSearch: Job? = null

        /** Function Extended in the "ViewExtend", for listener changes in the Search View , and repeat in 0.5 with a Job for get the last Value */
        searchView.onQueryTextChanged { queryChanged ->
            jobSearch?.cancel()
            jobSearch = MainScope().launch {
                delay(JOB_DELAY)
                queryChanged.let {
                    viewModel.setSearchQuery(queryChanged)
                }
            }
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.category_detail -> {
                    openDetailCategory()
                    true
                }
                R.id.hide_completed -> {
                    if (hide) {
                        viewModel.setHide(false)
                    } else {
                        viewModel.setHide(true)
                    }
                    true
                }
                else -> false
            }
        }

    }

    /** Open the Dialog Fragment for create category */
    private fun setupCreateCategory() {
        val createCategory = binding.categoryFilter.addCategory

        createCategory.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }


    /** Create the Chip Group with Category from Room */
    @SuppressLint("InflateParams")
    private fun setupChipGroup() {

        val categoryChipGroup = binding.categoryFilter.chipGroupCategory

        viewModel.category.observe(this) { categoryList ->

            with(categoryChipGroup) {
                removeAllViews()

                //I use for Each Indexed because i need know when the index is 0 to create the "All" option
                categoryList.forEachIndexed { index, category ->

                    val chip = layoutInflater.inflate(R.layout.single_chip, null) as Chip

                    if (index == 0) {
                        chip.text = getString(R.string.all)
                        addView(chip)
                    } else {
                        chip.text = category.categoryName
                        addView(chip)
                    }

                    //Select the first chip "All" when the app init
                    categoryChipGroup.check(categoryChipGroup.getChildAt(0).id)

                    /** Check which Chip was Selected */
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            if (chip.text.toString() == getString(R.string.all)) {
                                viewModel.setCategoryFilter("")
                            } else {
                                viewModel.setCategoryFilter(chip.text.toString())
                            }
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