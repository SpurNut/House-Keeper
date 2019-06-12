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

    @Query("SELECT * from task_photo INNER JOIN task_table ON task_photo.task_id == task_table.id WHERE task_table.task_completed == 1")
    fun getAllArchivedImages(): LiveData<List<TaskPhoto>>
}