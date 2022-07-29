package com.bootcamp.todoeasy.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityMainBinding
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

        setupFabButton()
        setupChipGroup()

    }

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


    private fun setupFabButton() {
        val fabCreateTask = binding.floatingButtonCreateTask

        fabCreateTask.setOnClickListener {

            val navController =
                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.modalBottomSheet)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val searchQuery = menu.findItem(R.id.action_search)
        val searchView = searchQuery.actionView as SearchView


        searchView.onQueryTextChanged { queryChanged ->
            viewModel.updateSearchQuery(queryChanged)
            viewModel.updateTaskWithCategory()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.hide_completed -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}