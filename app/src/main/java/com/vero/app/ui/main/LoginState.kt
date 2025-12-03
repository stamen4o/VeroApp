package com.vero.app.ui.main

data class LoginState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String? = null
)
