package com.vero.app.data.repository

import com.vero.app.data.local.AppDatabase
import com.vero.app.data.local.entity.TaskEntity
import com.vero.app.data.remote.ApiService
import com.vero.app.data.remote.RetrofitClient
import com.vero.app.data.remote.TaskResponse
import kotlin.collections.map

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
        val token = response.oauth.access_token
        println("LOGIN: token received = $token")
        return token
    }

    suspend fun refreshTasks(token: String): List<TaskEntity> {
        return try {
            println("TASKS: requesting remote tasks with token $token")
            val remote = api.getTasks("Bearer $token")
            println("TASKS: remote count = ${remote.size}")
            saveTasks(remote)
        } catch (e: Exception) {
            println("TASKS: error: ${e.message}")

            // Token expired? (API returns 401)
            if (e.message?.contains("401") == true) {
                println("TASKS: token expired → logging in again")
                val newToken = login()
                val retry = api.getTasks("Bearer $newToken")
                println("TASKS: retry count = ${retry.size}")
                saveTasks(retry)
            } else {
                throw e
            }
        }
    }

    private suspend fun saveTasks(remote: List<TaskResponse>): List<TaskEntity> {
        val mapped = remote.map {
            TaskEntity(
                task = it.task,
                title = it.title,
                description = it.description,
                colorCode = it.colorCode
            )
        }

        dao.insertTasks(mapped)

        println("TASKS: saved ${mapped.size} items locally")
        return mapped
    }

    suspend fun getTasksLocal() = dao.getAllTasks()

    suspend fun searchTasks(query: String) = dao.searchTasks(query)
}
