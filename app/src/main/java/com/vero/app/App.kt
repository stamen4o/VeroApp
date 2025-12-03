package com.vero.app

import android.app.Application
import androidx.room.Room
import com.vero.app.data.local.AppDatabase

class App : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "vero_db"
        ).build()
    }
}
