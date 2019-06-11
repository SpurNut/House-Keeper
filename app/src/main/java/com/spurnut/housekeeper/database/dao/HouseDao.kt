package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.spurnut.housekeeper.database.enity.House


@Dao
interface HouseDao {

    @Insert
    suspend fun insert(house: House)

    @Update
    suspend fun update(house: House)

    @Delete
    suspend fun delete(house: House)

//    @Query("SELECT * from house_table")
//    suspend fun getAllHouses(): LiveData<List<House>>
}