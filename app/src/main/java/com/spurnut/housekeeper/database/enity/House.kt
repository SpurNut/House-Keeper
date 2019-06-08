package com.spurnut.housekeeper.database.enity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "house_table")
data class House(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @NonNull @ColumnInfo(name = "street_name") val streetName: String,
        @NonNull @ColumnInfo(name = "street_number") val streetNumber: String
)