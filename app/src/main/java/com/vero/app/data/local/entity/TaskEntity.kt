package com.vero.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val task: String,
    val title: String?,
    val description: String?,
    val colorCode: String?
)


