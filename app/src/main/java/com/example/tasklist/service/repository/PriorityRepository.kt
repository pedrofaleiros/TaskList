package com.example.tasklist.service.repository

import android.content.Context
import com.example.tasklist.R
import com.example.tasklist.service.listener.APIListener
import com.example.tasklist.service.model.PriorityModel
import com.example.tasklist.service.repository.local.TaskDatabase
import com.example.tasklist.service.repository.remote.PriorityService
import com.example.tasklist.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()

        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(key: Int, value: String) {
            cache[key] = value
        }
    }

    fun getDescription(id: Int): String {
        var description = PriorityRepository.getDescription(id)
        if (description == "") {
            description = database.getDescription(id)
            PriorityRepository.setDescription(id, description)
        }
        return description
    }

    fun list(listener: APIListener<List<PriorityModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.list()
        executeCall(call, listener)
    }

    fun list(): List<PriorityModel> {
        return database.list()
    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }
}