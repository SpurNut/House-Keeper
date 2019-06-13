package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HouseViewModel (application: Application) : AndroidViewModel(application), CoroutineScope by MainScope() {
    val allHouses: LiveData<List<House>>
    private val repository: DataRepository

    init {
        repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application, viewModelScope))
        allHouses = repository.allHouses
    }

    fun insert(house: House) = viewModelScope.launch {
        repository.insert(house)
    }

    fun update(house: House) = viewModelScope.launch {
        repository.update(house)
    }

}