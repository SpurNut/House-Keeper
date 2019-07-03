package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import kotlinx.coroutines.*

class TaskOverviewViewModel (application: Application) : AndroidViewModel(application), CoroutineScope by MainScope(), TViewModel {

    private val repository: DataRepository

    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allTasks: LiveData<List<Task>>

    init {
        repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application, viewModelScope))
        allTasks = repository.allTasks
    }

    fun insert(task: Task): Int = runBlocking {
        repository.insert(task).toInt()
    }

    override fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun getHouseById(house_id: Int) : LiveData<House> {
        return repository.getHouseById(house_id)
    }


}