package com.vero.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vero.app.ui.tasks.TasksActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Directly open the tasks screen
        startActivity(Intent(this, TasksActivity::class.java))

        // We don't need MainActivity in the back stack
        finish()
    }
}
