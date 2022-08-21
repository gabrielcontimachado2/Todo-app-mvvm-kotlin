package com.bootcamp.todoeasy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.relations.CategoryWithTask
import com.bootcamp.todoeasy.databinding.CardCategoryDetailBinding
import com.bootcamp.todoeasy.util.diffUtil.DiffUtilCategory

class CategoryAdapter :
    androidx.recyclerview.widget.ListAdapter<CategoryWithTask, CategoryAdapter.CategoryViewHolder>(DiffUtilCategory()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            CardCategoryDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private var onOptionsClickListener: ((category: Category, View) -> Unit)? = null

    fun setonCheckClickListener(listener: (category: Category, View) -> Unit) {
        onOptionsClickListener = listener
    }


    inner class CategoryViewHolder(private val binding: CardCategoryDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryWithTask: CategoryWithTask) {
            binding.apply {

                textViewTitleCategory.text = categoryWithTask.category.categoryName
                textViewNumberTasks.text = categoryWithTask.taskList.size.toString()

                menuCategory.setOnClickListener { view ->
                    onOptionsClickListener?.let { it(categoryWithTask.category, view) }
                }

            }
        }
    }
}