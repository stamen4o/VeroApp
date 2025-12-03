package com.vero.app.data.repository

import com.vero.app.data.remote.ApiService
import com.vero.app.data.remote.RetrofitClient

class TaskRepository(
    private val api: ApiService = RetrofitClient.api
) {

    suspend fun login(): String {
        val response = api.login(
            authHeader = "Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz",
            credentials = mapOf(
                "username" to "365",
                "password" to "1"
            )
        )

        return response.oauth.access_token
    }

    suspend fun getTasks(token: String) =
        api.getTasks("Bearer $token")
}
