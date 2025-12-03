package com.vero.app.data.local

import com.vero.app.data.local.dao.TaskDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.vero.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
