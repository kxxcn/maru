package dev.kxxcn.maru.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Direction")
data class Direction(
    @ColumnInfo(name = "json") var json: String = "",
    @ColumnInfo(name = "longitude") var longitude: Double = 0.0,
    @ColumnInfo(name = "latitude") var latitude: Double = 0.0,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)
