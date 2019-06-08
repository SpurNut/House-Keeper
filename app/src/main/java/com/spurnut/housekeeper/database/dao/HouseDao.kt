package com.spurnut.housekeeper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.spurnut.housekeeper.database.enity.House


@Dao
interface HouseDao {

    @Insert
    fun insert(house: House)

    @Update
    fun update(house: House)

    @Delete
    fun delete(house: House)

    @Query("SELECT * from house_table")
    fun getAllHouses(): LiveData<List<House>>
}