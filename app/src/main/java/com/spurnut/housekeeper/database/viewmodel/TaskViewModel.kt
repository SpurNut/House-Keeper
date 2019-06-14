package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto


import kotlinx.coroutines.*

class TaskViewModel(application: Application, val task_id: Int) : AndroidViewModel(application), TViewModel {

    private val repository: DataRepository
    val task: LiveData<Task>
    val images: LiveData<List<TaskPhoto>>

    init {
        repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application, viewModelScope))
        task = repository.getTaskById(task_id)
        images = repository.getTaskPhotosById(task_id)
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    override fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun insert(taskPhoto: TaskPhoto) = viewModelScope.launch {
        repository.insert(taskPhoto)
    }

    fun delete(taskPhoto: TaskPhoto) = viewModelScope.launch {
        repository.delete(taskPhoto)
    }

    fun getHouseById(house_id: Int) : LiveData<House> {
        return repository.getHouseById(house_id)
    }
}