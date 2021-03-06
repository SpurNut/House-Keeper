package com.spurnut.housekeeper.database.viewmodel

import android.app.Application
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spurnut.housekeeper.database.DataRepository
import com.spurnut.housekeeper.database.HouseKeeperRoomDatabase
import com.spurnut.housekeeper.database.enity.House
import kotlinx.coroutines.*

class HouseViewModel (application: Application) : AndroidViewModel(application), CoroutineScope by MainScope() {
    val allHouses: LiveData<List<House>>
    private val repository = DataRepository(HouseKeeperRoomDatabase.getDatabase(application, viewModelScope))

    init {
        allHouses = repository.allHouses
    }

    fun insert(house: House) = viewModelScope.launch {
        repository.insert(house)
    }

    fun update(house: House) = viewModelScope.launch {
        repository.update(house)
    }

    fun delete(house: House) = viewModelScope.launch {
        try {
            repository.delete(house)
        } catch (exception: android.database.sqlite.SQLiteConstraintException) {
            //Todo show user that this house is used by tasks and therefore can not be deleted
        }
    }

}