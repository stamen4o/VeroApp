package com.vero.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.app.App
import com.vero.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: TaskRepository = TaskRepository(
        db = App.db
    )
) : ViewModel() {


    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login() {
        _state.value = LoginState(isLoading = true)

        viewModelScope.launch {
            try {
                val token = repository.login()
                _state.value = LoginState(token = token)
            } catch (e: Exception) {
                _state.value = LoginState(error = e.message ?: "Unknown error")
            }
        }
    }
}
