package com.vero.app.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.app.App
import com.vero.app.data.repository.TaskRepository

class TaskSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = TaskRepository(db = App.db)

    override suspend fun doWork(): Result {
        return try {
            // 1. Login (suspend OK now)
            val token = repository.login()

            // 2. Sync tasks (also suspend)
            repository.refreshTasks(token)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
