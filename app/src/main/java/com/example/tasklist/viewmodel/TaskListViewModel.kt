package com.example.tasklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.service.listener.APIListener
import com.example.tasklist.service.model.TaskModel
import com.example.tasklist.service.model.ValidationModel
import com.example.tasklist.service.repository.PriorityRepository
import com.example.tasklist.service.repository.TaskRepository

class
TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun status(id: Int, complete: Boolean) {

        val listener = object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list()
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }
        }

        if (complete) {
            taskRepository.complete(id, listener)
        } else {
            taskRepository.undo(id, listener)
        }
    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)

            }
        })
    }

    fun list() {
        taskRepository.list(object : APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }
                _tasks.value = result
            }

            override fun onFailure(message: String) {
            }
        })
    }
}