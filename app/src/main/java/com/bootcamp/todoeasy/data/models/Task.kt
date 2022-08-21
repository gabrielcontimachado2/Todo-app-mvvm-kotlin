package com.bootcamp.todoeasy.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(
    tableName = "task",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("categoryName"),
        childColumns = arrayOf("categoryName"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Task(
    @ColumnInfo(name = "idTask")
    @PrimaryKey
    val idTask: String,
    val name: String,
    val description: String,
    val status: Boolean,
    val priority: Int,
    val categoryName: String,
    val date: Date = Date(99 - 99 - 9999),
    val hour: String,
    val created: Date,
    val reminder: Boolean,
    val interval: String
) : Parcelable
