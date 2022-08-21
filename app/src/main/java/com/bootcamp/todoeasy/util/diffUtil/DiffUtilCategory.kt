package com.bootcamp.todoeasy.util.diffUtil


import androidx.recyclerview.widget.DiffUtil
import com.bootcamp.todoeasy.data.relations.CategoryWithTask

class DiffUtilCategory : DiffUtil.ItemCallback<CategoryWithTask>() {

    override fun areItemsTheSame(oldItem: CategoryWithTask, newItem: CategoryWithTask): Boolean {
        return oldItem.category.idCategory == oldItem.category.idCategory
    }

    override fun areContentsTheSame(oldItem: CategoryWithTask, newItem: CategoryWithTask): Boolean {
        return oldItem == newItem
    }
}