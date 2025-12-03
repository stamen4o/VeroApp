package com.vero.app.data.repository

import com.vero.app.data.local.AppDatabase
import com.vero.app.data.local.entity.TaskEntity
import com.vero.app.data.remote.ApiService
import com.vero.app.data.remote.RetrofitClient

class TaskRepository(
    private val api: ApiService = RetrofitClient.api,
    private val db: AppDatabase
) {
    private val dao = db.taskDao()

    suspend fun login(): String {
        println("LOGIN: starting login request")
        val response = api.login(
            authHeader = "Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz",
            credentials = mapOf("username" to "365", "password" to "1")
        )
        println("LOGIN: token received = ${response.oauth.access_token}")
        return response.oauth.access_token
    }

    suspend fun refreshTasks(token: String) {
        println("TASKS: requesting remote tasks with token: $token")
        val remote = api.getTasks("Bearer $token")
        println("TASKS: remote count = ${remote.size}")
        val mapped = remote.map {
            TaskEntity(
                task = it.task ?: "",
                title = it.title ?: "",
                description = it.description ?: "",
                colorCode = it.colorCode ?: "#000000"
            )
        }

        println("TASKS: mapped ${mapped.size} items, saving to DB")
        dao.insertTasks(mapped)
        val localCount = dao.getAllTasks().size
        println("TASKS: local DB count after insert = $localCount")
    }

    suspend fun getTasksLocal() = dao.getAllTasks()

    suspend fun searchTasks(query: String) = dao.searchTasks(query)
}

