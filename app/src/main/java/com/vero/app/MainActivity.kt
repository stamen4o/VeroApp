package com.vero.app

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vero.app.ui.main.LoginViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.login()

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when {
                    state.isLoading -> {
                        Log.d("Login", "Logging in...")
                    }
                    state.token != null -> {
                        Log.d("Login", "Token: ${state.token}")
                    }
                    state.error != null -> {
                        Log.e("Login", "Error: ${state.error}")
                    }
                }
            }
        }
    }
}

