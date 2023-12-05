package com.example.tasklist.view.viewholder

import android.app.AlertDialog
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.databinding.RowTaskListBinding
import com.example.tasklist.service.model.TaskModel
import com.example.tasklist.R
import com.example.tasklist.service.listener.TaskListener
import com.example.tasklist.service.repository.PriorityRepository
import java.text.SimpleDateFormat

class TaskViewHolder(
    private val itemBinding: RowTaskListBinding,
    val listener: TaskListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(task: TaskModel) {
        itemBinding.textDescription.text = task.description
        itemBinding.textPriority.text = task.priorityDescription

        val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate)
        val dueDate = SimpleDateFormat("dd/MM/yyyy").format(date)
        itemBinding.textDueDate.text = dueDate

        if (task.complete) {
            itemBinding.imageTask.setImageResource(R.drawable.circle_checked)
        } else {
            itemBinding.imageTask.setImageResource(R.drawable.circle)
        }

        itemBinding.textDescription.setOnClickListener { listener.onListClick(task.id) }
        itemBinding.imageTask.setOnClickListener {
            if (task.complete) {
                listener.onUndoClick(task.id)
            } else {
                listener.onCompleteClick(task.id)
            }
        }

        itemBinding.imageTask.setOnClickListener {
            if (task.complete) {
                listener.onUndoClick(task.id)
            } else {
                listener.onCompleteClick(task.id)
            }
        }

        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context).setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which -> listener.onDeleteClick(task.id) }
                .setNeutralButton(R.string.cancelar, null).show()
            true
        }
    }
}