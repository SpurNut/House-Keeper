package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ArchivedTaskViewModel (application: Application) : AndroidViewModel(application), CoroutineScope by MainScope(), TViewModel {

    private val repository: DataRepository
    val allArchivedTasks: LiveData<List<Task>>

    init {
        repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application,viewModelScope))
        allArchivedTasks = repository.allArchivedTasks
    }


    override fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }
}