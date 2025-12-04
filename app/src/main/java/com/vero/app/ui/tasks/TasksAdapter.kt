package com.vero.app.ui.tasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
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

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        val b = holder.binding

        // Basic text
        b.taskName.text = task.task ?: ""
        b.taskTitle.text = task.title ?: ""
        b.taskDescription.text = task.description ?: ""

        // Clean hex: remove "#" if present
        val rawColor = task.colorCode?.replace("#", "") ?: "FFFFFF"

        val parsedColor = try {
            Color.parseColor("#$rawColor")
        } catch (e: Exception) {
            Color.LTGRAY
        }


        // 🔥 Set entire card background as the task color
        (b.root as CardView).setCardBackgroundColor(parsedColor)

        // Auto-adjust text colors for readability
        val textColor = if (isColorDark(parsedColor)) Color.WHITE else Color.BLACK
        b.taskName.setTextColor(textColor)
        b.taskTitle.setTextColor(textColor)
        b.taskDescription.setTextColor(textColor)
    }

    override fun getItemCount(): Int = items.size

    private fun isColorDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) +
                    0.587 * Color.green(color) +
                    0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}
