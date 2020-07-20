package dev.kxxcn.maru.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.kxxcn.maru.data.Direction

@Dao
interface DirectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirection(direction: Direction)

    @Query("SELECT json FROM Direction WHERE longitude = :longitude AND latitude = :latitude")
    suspend fun getDirection(longitude: Double, latitude: Double): String
}
