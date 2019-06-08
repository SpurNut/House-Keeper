package com.spurnut.housekeeper.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.User

class DataRepository(val appDatabase: HouseKeeperRoomDatabase) {

    val allTasks: LiveData<List<Task>> = appDatabase.taskDao().getAllTasks()
    val allUsers: LiveData<List<User>> = appDatabase.userDao().getAllUsers()
    val allHouses: LiveData<List<House>> = appDatabase.houseDao().getAllHouses()

    @WorkerThread
    fun insert(task: Task) {
        appDatabase.taskDao().insert(task)
    }

    @WorkerThread
    fun update(task: Task) {
        appDatabase.taskDao().update(task)
    }

    @WorkerThread
    fun insert(user: User){
        appDatabase.userDao().insert(user)
    }

    @WorkerThread
    fun update(user: User) {
        appDatabase.userDao().update(user)
    }

    @WorkerThread
    fun insert(house: House) {
        appDatabase.houseDao().insert(house)
    }

    @WorkerThread
    fun update(house: House) {
        appDatabase.houseDao().update(house)
    }
}