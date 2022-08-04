package com.bootcamp.todoeasy.ui.activitys.detailTask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.ActivityDetailTaskBinding
import com.bootcamp.todoeasy.util.FormatDate
import com.bootcamp.todoeasy.util.onQueryTextChanged

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding
    private val args: DetailTaskActivityArgs by navArgs()
    private val viewModel: DetailTaskViewModel by viewModels()

    private lateinit var taskDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupToolbar()
        observerTask()

    }

    private fun observerTask() {
        val task = args.task

        val descriptionEditable = Editable.Factory.getInstance().newEditable(task.description)
        val titleEditable = Editable.Factory.getInstance().newEditable(task.name)
        val formatDate = FormatDate()

        binding.ediTextCategoryDescription.text = descriptionEditable
        binding.ediTextTitle.text = titleEditable
        binding.expirationDate.textViewDrawableDate.text = formatDate.formatDate(task.date)
        binding.hour.textViewDrawableHour.text = task.hour
    }

    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                true
            }
            R.id.complete_task -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}