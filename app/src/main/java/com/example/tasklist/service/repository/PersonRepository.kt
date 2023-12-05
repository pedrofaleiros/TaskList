package com.example.tasklist.service.repository

import android.content.Context
import android.util.Log
import com.example.tasklist.R
import com.example.tasklist.service.contants.TaskConstants
import com.example.tasklist.service.listener.APIListener
import com.example.tasklist.service.model.PersonModel
import com.example.tasklist.service.repository.remote.PersonService
import com.example.tasklist.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.login(email, password)
        executeCall(call, listener)
    }
}