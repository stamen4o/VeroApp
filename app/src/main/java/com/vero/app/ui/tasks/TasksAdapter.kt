package com.vero.app.ui.tasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vero.app.data.local.entity.TaskEntity
import com.vero.app.databinding.ItemTaskBinding

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val items = mutableListOf<TaskEntity>()

    fun submitList(list: List<TaskEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskEntity) {
            binding.taskName.text = task.task
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description

            // Set colorCode background
            runCatching {
                binding.colorBadge.setBackgroundColor(Color.parseColor(task.colorCode))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
