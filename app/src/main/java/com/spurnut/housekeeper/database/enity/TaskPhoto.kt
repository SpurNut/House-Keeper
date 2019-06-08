package com.spurnut.housekeeper.database.enity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_photo",
        foreignKeys = [
            ForeignKey(entity = Task::class,
                    parentColumns = ["id"],
                    childColumns = ["task_id"],
                    onDelete = CASCADE)])
data class TaskPhoto(
        @PrimaryKey val uid: String,
        @NonNull @ColumnInfo(name = "task_id") val taskId: String)