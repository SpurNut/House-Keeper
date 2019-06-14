package com.spurnut.housekeeper.database

import androidx.lifecycle.LiveData
import com.spurnut.housekeeper.database.enity.*

class DataRepository(val appDatabase: HouseKeeperRoomDatabase) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allTasks: LiveData<List<Task>> = appDatabase.taskDao().getAllTasks()
    val allArchivedImages: LiveData<List<TaskPhoto>> = appDatabase.taskPhotoDao().getAllArchivedImages()
    val allArchivedTasks: LiveData<List<Task>> = appDatabase.taskDao().getAllArchivedTasks()
//    val allUsers: LiveData<List<User>> = appDatabase.userDao().getAllUsers()
    val allHouses: LiveData<List<House>> = appDatabase.houseDao().getAllHouses()


    // The suspend modifier tells the compiler that this must be called from a
    // coroutine or another suspend function.
    // This ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    suspend fun insert(task: Task): Long {
        return appDatabase.taskDao().insert(task)
    }

    suspend fun update(task: Task) {
        appDatabase.taskDao().update(task)
    }

    fun getTaskById(task_id: Int): LiveData<Task> {
        return appDatabase.taskDao().getTaskById(task_id)
    }

    fun getTaskPhotosById(task_id: Int): LiveData<List<TaskPhoto>> {
        return appDatabase.taskPhotoDao().getTaskPhotosById(task_id)
    }

    suspend fun insert(taskPhoto: TaskPhoto) {
        appDatabase.taskPhotoDao().insert(taskPhoto)
    }

    suspend fun insert(user: User){
        appDatabase.userDao().insert(user)
    }

    suspend fun update(user: User) {
        appDatabase.userDao().update(user)
    }

    suspend fun insert(house: House) {
        appDatabase.houseDao().insert(house)
    }

    suspend fun update(house: House) {
        appDatabase.houseDao().update(house)
    }

    suspend fun delete(house: House) {
        appDatabase.houseDao().delete(house)
    }

    suspend fun delete(taskPhoto: TaskPhoto) {
        appDatabase.taskPhotoDao().delete(taskPhoto)
    }

    fun deleteAllArchived() {
        appDatabase.taskDao().deleteAllArchived()
    }

    fun getHouseById(house_id: Int): LiveData<House> {
        return appDatabase.houseDao().getHouseById(house_id)
    }
}