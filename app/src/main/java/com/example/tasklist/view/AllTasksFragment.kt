package com.example.tasklist.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.databinding.FragmentAllTasksBinding
import com.example.tasklist.service.contants.TaskConstants
import com.example.tasklist.service.listener.TaskListener
import com.example.tasklist.view.adapter.TaskAdapter
import com.example.tasklist.viewmodel.TaskListViewModel
import kotlin.math.log

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TaskAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {

        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        val listener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                viewModel.status(id, true)
            }

            override fun onUndoClick(id: Int) {
                viewModel.status(id, false)
            }
        }

        val taskfilter: Int = requireArguments().getInt("taskfilter", 0)
        Log.d("PEDRO", taskfilter.toString())

        adapter.attachListener(listener)

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateTasks(it)
        }

        viewModel.delete.observe(viewLifecycleOwner) {
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}