package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.spurnut.housekeeper.database.enity.Task

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * from task_table")
    fun getAllTasks(): LiveData<List<Task>>
}