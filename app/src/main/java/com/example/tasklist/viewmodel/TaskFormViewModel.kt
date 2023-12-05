package com.example.tasklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasklist.service.listener.APIListener
import com.example.tasklist.service.model.PriorityModel
import com.example.tasklist.service.model.TaskModel
import com.example.tasklist.service.model.ValidationModel
import com.example.tasklist.service.repository.PriorityRepository
import com.example.tasklist.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepo = PriorityRepository(application.applicationContext)
    private val taskRepo = TaskRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    private val _taskLoad = MutableLiveData<ValidationModel>()
    val taskLoad: LiveData<ValidationModel> = _taskLoad

    fun loadPriorities() {
        _priorityList.value = priorityRepo.list()
    }

    fun load(id: Int) {
        taskRepo.load(id, object : APIListener<TaskModel> {
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }

            override fun onFailure(message: String) {
                _taskLoad.value = ValidationModel(message)
            }

        })
    }

    fun save(task: TaskModel) {
        val listener = object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message)
            }
        }

        if (task.id == 0) {
            taskRepo.create(task, listener)
        } else {
            taskRepo.update(task, listener)
        }
    }
}