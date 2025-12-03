package com.vero.app.ui.tasks

import com.vero.app.data.local.entity.TaskEntity

data class TaskListState(
    val isLoading: Boolean = false,
    val tasks: List<TaskEntity> = emptyList(),
    val query: String = "",
    val error: String? = null
)
