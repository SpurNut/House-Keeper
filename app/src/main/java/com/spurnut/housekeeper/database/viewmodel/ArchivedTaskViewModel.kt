package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto
import android.util.Log
import kotlinx.coroutines.*
import java.io.File


class ArchivedTaskViewModel(application: Application) : AndroidViewModel(application), CoroutineScope by MainScope(), TViewModel {

    private val repository: DataRepository
    val allArchivedTasks: LiveData<List<Task>>
    val allArchivedImages: LiveData<List<TaskPhoto>>

    init {
        repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application, viewModelScope))
        allArchivedTasks = repository.allArchivedTasks
        allArchivedImages = repository.allArchivedImages
    }


    override fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteAllArchived() {
        viewModelScope.launch(Dispatchers.IO) {
            runBlocking {
                if (allArchivedImages.value != null) {
                    for (archived in allArchivedImages.value!!) {
                        val filePath = archived.uid
                        val file = File(filePath)
                        val deleted = file.delete()
                    }
                }
                repository.deleteAllArchived()
            }
        }
    }
}