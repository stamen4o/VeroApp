package com.vero.app.ui.tasks

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vero.app.R
import com.vero.app.data.local.entity.TaskEntity
import com.vero.app.databinding.ActivityTasksBinding
import com.vero.app.ui.qr.QrScanActivity


class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private val viewModel: TasksViewModel by viewModels()
    private val adapter = TasksAdapter()
    private var searchView: SearchView? = null

    private val qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val qrText = result.data?.getStringExtra("qr") ?: return@registerForActivityResult

                // Apply search
                searchView?.setQuery(qrText, true)
                viewModel.setQuery(qrText)
            }
        }
    private var allTasks: List<TaskEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        binding.tasksRecycler.layoutManager = LinearLayoutManager(this)
        binding.tasksRecycler.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                binding.swipeRefresh.isRefreshing = state.isLoading
                adapter.submitList(state.tasks)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh(includeRemote = true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView?.queryHint = "Search tasks…"

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText.orEmpty())
                return true
            }
        })

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_scan -> {
                val intent = Intent(this, QrScanActivity::class.java)
                qrLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun filterTasks(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.submitList(allTasks)
            return
        }

        val q = query.lowercase()

        val filtered = allTasks.filter {
            (it.task ?: "").lowercase().contains(q) ||
                    (it.title ?: "").lowercase().contains(q) ||
                    (it.description ?: "").lowercase().contains(q) ||
                    (it.colorCode ?: "").lowercase().contains(q)
        }


        adapter.submitList(filtered)
    }
}
