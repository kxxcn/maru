package dev.kxxcn.maru.data

import androidx.room.*
import dev.kxxcn.maru.view.days.DaysFilterType
import dev.kxxcn.maru.view.days.DaysFilterType.COUNT
import java.util.*

@Entity(
    tableName = "days",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Day(
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "date") var date: Long = 0L,
    @ColumnInfo(name = "type") var type: DaysFilterType = COUNT,
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "userId") var userId: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)
