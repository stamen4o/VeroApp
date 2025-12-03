package com.vero.app

import android.app.Application
import androidx.room.Room
import com.vero.app.data.local.AppDatabase

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
    }
}
