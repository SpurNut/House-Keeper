package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.spurnut.housekeeper.database.enity.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task) : Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from task_table WHERE task_completed == 0")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * from task_table WHERE id == :task_id")
    fun getTaskById(task_id: Int): LiveData<Task>
}