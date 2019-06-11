package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.spurnut.housekeeper.database.enity.TaskPhoto

@Dao

interface TaskPhotoDao {

    @Query("SELECT * from task_photo WHERE task_id == :task_id")
    fun getTaskPhotosById(task_id: Int): LiveData<List<TaskPhoto>>

    @Insert
    suspend fun insert(taskPhoto: TaskPhoto)

    @Delete
    suspend fun delete(taskPhoto: TaskPhoto)
}