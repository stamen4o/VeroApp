package com.vero.app

import android.app.Application
import androidx.room.Room
import com.vero.app.data.local.AppDatabase
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.vero.app.work.TaskSyncWorker

class App : Application() {

    companion object {
        lateinit var db: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,           // <-- FIXED
            AppDatabase::class.java,
            "vero_db"
        )
            .fallbackToDestructiveMigration() // <-- recommended while developing
            .build()
        val request = PeriodicWorkRequestBuilder<TaskSyncWorker>(
            60, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "task_sync_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

    }
}
