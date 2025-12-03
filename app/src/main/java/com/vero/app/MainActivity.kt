package com.vero.app

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vero.app.databinding.ActivityMainBinding
import com.vero.app.ui.tasks.TasksAdapter
import com.vero.app.ui.tasks.TasksViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var adapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar
        setSupportActionBar(binding.toolbar)

        // Setup recycler
        adapter = TasksAdapter()
        binding.tasksRecycler.layoutManager = LinearLayoutManager(this)
        binding.tasksRecycler.adapter = adapter

        // Swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh(includeRemote = true)
        }

        // Observe state
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                binding.swipeRefresh.isRefreshing = state.isLoading

                if (state.error != null) {
                    Log.e("Tasks", "Error: ${state.error}")
                }

                adapter.submitList(state.tasks)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Search tasks…"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            R.id.action_qr -> {
                // TODO: QR Scanner will be added next
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
