package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.spurnut.housekeeper.database.enity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("Select * from user_table")
    fun getAllUsers(): LiveData<List<User>>
}