package com.example.tasklist.service.model

class ValidationModel(message: String = "") {

    private var status: Boolean = true
    private var validationMessage: String = ""

    init {
        if (message != "") {
            status = false
            validationMessage = message
        }
    }

    fun status(): Boolean = status
    fun message(): String = validationMessage
}