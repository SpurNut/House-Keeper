package com.spurnut.housekeeper.converter

import androidx.room.TypeConverter
import com.spurnut.housekeeper.database.enity.UrgencyImportantQuadrant

class UrgencyImportantQuadrantConverter {

    @TypeConverter
    fun toUrgencuImportanceQuadrant(value: Int?): UrgencyImportantQuadrant? {
        when (value) {
            1 -> return UrgencyImportantQuadrant.URGENT_IMPORTANT
            2 -> return UrgencyImportantQuadrant.URGENT_NOT_IMPORTANT
            3 -> return UrgencyImportantQuadrant.NOT_URGENT_IMPORTANT
            4 -> return UrgencyImportantQuadrant.NOT_URGEND_NOT_IMPORTANT
        }
        return null
    }

    @TypeConverter
    fun toInt(urgencyImportance: UrgencyImportantQuadrant?): Int? {
        when (urgencyImportance) {
            UrgencyImportantQuadrant.URGENT_IMPORTANT -> return 1
            UrgencyImportantQuadrant.URGENT_NOT_IMPORTANT -> return 2
            UrgencyImportantQuadrant.NOT_URGENT_IMPORTANT -> return 3
            UrgencyImportantQuadrant.NOT_URGEND_NOT_IMPORTANT -> return 4
        }
        return null
    }
}