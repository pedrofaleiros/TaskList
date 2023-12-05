package com.example.tasklist.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasklist.R
import com.example.tasklist.databinding.ActivityTaskFormBinding
import com.example.tasklist.service.contants.TaskConstants
import com.example.tasklist.service.model.PriorityModel
import com.example.tasklist.service.model.TaskModel
import com.example.tasklist.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = mutableListOf()

    private var tId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()

        setContentView(binding.root)

        loadDataFromActivity()

        observe()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if (bundle != null) {
            tId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(tId)
            binding.buttonSave.text = "Salvar"
        }
    }

    private fun getIndex(id: Int): Int {
        var index = 0
        for (l in listPriority) {
            if (l.id == id) {
                break
            }
            index++
        }
        return index
    }

    private fun observe() {
        viewModel.priorityList.observe(this) {
            listPriority = it
            val list = mutableListOf<String>()

            for (p in it) {
                list.add(p.description)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this) {
            if (it.status()) {

                if (tId == 0) {
                    toast("Criada com sucesso")
                } else {
                    toast("Atualizada com sucesso")
                }

                finish()
            } else {
                toast(it.message())
            }
        }

        viewModel.task.observe(this) {
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete

            binding.spinnerPriority.setSelection(getIndex(it.priorityId))

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            val dueDate = SimpleDateFormat("dd/MM/yyyy").format(date)
            binding.buttonDate.text = dueDate
        }

        viewModel.taskLoad.observe(this) {
            if (it.status()) {
                toast(it.message())
                finish()
            }
        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = tId
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()
            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id
        }

        viewModel.save(task)
    }

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dueDate = dateFormat.format(calendar.time)

        binding.buttonDate.text = dueDate
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

}