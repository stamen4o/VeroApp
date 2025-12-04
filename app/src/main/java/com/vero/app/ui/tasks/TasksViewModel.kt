package com.vero.app.ui.tasks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
        refresh(includeRemote = false)   // FIRST load local ONLY
        refresh(includeRemote = true)    // THEN try remote
    }


    fun refresh(includeRemote: Boolean = true, online: Boolean = true) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                // Only call remote API if includeRemote == true AND device is online
                if (includeRemote && online) {

                    // Login if token missing
                    if (token == null) {
                        token = repository.login()
                    }

                    // Perform remote sync
                    val refreshed = repository.refreshTasks(token!!)

                    // Keep ViewModel's token in sync in case repository refreshed it
                    if (refreshed.isNotEmpty()) {
                        token = token
                    }
                }

                // Always load local DB (works offline!)
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
