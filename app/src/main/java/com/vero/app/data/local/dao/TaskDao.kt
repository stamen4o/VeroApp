package com.vero.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vero.app.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("""
        SELECT * FROM tasks
        WHERE task LIKE '%' || :query || '%' 
        OR title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        OR colorCode LIKE '%' || :query || '%'
    """)
    suspend fun searchTasks(query: String): List<TaskEntity>
}
