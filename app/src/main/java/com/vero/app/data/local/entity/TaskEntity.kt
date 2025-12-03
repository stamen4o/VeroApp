package com.vero.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val task: String, // unique task ID

    val title: String,
    val description: String,
    val colorCode: String
)
