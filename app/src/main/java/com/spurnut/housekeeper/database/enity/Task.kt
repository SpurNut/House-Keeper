package com.spurnut.housekeeper.database.enity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

enum class UrgencyImportantQuadrant {
    URGENT_IMPORTANT, URGENT_NOT_IMPORTANT,
    NOT_URGENT_IMPORTANT, NOT_URGEND_NOT_IMPORTANT
}

@Entity(tableName = "task_table")
data class Task(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "task_completed") val completed: Boolean,
        @ColumnInfo(name = "task_title") val title: String,
        @ColumnInfo(name = "urgency") val urgency: UrgencyImportantQuadrant,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "dueDate") val dueDate: Date?)

