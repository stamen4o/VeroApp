package com.vero.app.ui.tasks

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vero.app.R
import com.vero.app.data.local.entity.TaskEntity
import com.vero.app.databinding.ActivityTasksBinding

class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private val viewModel: TasksViewModel by viewModels()
    private val adapter = TasksAdapter()

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

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Search tasks..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText ?: "")
                return true
            }
        })


        return true
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
