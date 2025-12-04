package com.vero.app.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.app.App
import com.vero.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {

    private val repository = TaskRepository(db = App.db)

    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state

    private var token: String? = null

    init {
        refresh(includeRemote = true)
    }

    fun refresh(includeRemote: Boolean = true) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                if (includeRemote) {
                    if (token == null) token = repository.login()

                    val result = repository.refreshTasks(token!!)
                    // If refreshTasks got a NEW token, update ours
                    token = token ?: ""
                }

                val tasks = if (_state.value.query.isBlank()) {
                    repository.getTasksLocal()
                } else {
                    repository.searchTasks(_state.value.query)
                }

                _state.value = _state.value.copy(
                    tasks = tasks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }


    fun setQuery(query: String) {
        _state.value = _state.value.copy(query = query)
        refresh(includeRemote = false)
    }
}
