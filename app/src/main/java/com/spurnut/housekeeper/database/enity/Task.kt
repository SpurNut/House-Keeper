package com.spurnut.housekeeper.database.enity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

enum class UrgencyImportantQuadrant {
    URGENT_IMPORTANT, URGENT_NOT_IMPORTANT,
    NOT_URGENT_IMPORTANT, NOT_URGEND_NOT_IMPORTANT
}

@Entity(tableName = "task_table", foreignKeys = [
    ForeignKey(entity = House::class,
            parentColumns = ["id"],
            childColumns = ["house_id"],
            onDelete = ForeignKey.NO_ACTION)])
data class Task(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "task_completed") val completed: Boolean,
        @ColumnInfo(name = "task_title") val title: String,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "house_id") val houseId: Int?,
        @ColumnInfo(name = "dueDate") val dueDate: Date?,
        @ColumnInfo(name = "reminderDate") val reminderDate: Date?)

