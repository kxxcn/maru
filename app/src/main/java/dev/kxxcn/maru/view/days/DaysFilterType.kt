package dev.kxxcn.maru.view.days

import androidx.room.TypeConverter

enum class DaysFilterType {

    /**
     * 날짜수
     */
    COUNT,

    /**
     * 디데이
     */
    REMAIN
}

class DaysTypeConverter {

    @TypeConverter
    fun fromDaysFilterType(value: DaysFilterType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toDaysFilterType(value: Int): DaysFilterType {
        return when (value) {
            0 -> DaysFilterType.COUNT
            else -> DaysFilterType.REMAIN
        }
    }
}
