package dev.kxxcn.maru.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.kxxcn.maru.data.Day

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayAll(days: List<Day>)

    @Delete
    suspend fun deleteDay(vararg day: Day): Int
}
