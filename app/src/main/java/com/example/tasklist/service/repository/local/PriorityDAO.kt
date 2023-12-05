package com.example.tasklist.service.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tasklist.service.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert
    fun save(list: List<PriorityModel>)

    @Query("select * from Priority")
    fun list(): List<PriorityModel>

    @Query("DELETE FROM Priority")
    fun clear()

    @Query("SELECT description from Priority WHERE id = :id")
    fun getDescription(id: Int): String
}