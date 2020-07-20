package dev.kxxcn.maru.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("taskId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Account(
    @ColumnInfo(name = "husband") var husband: Long = 0L,
    @ColumnInfo(name = "wife") var wife: Long = 0L,
    @ColumnInfo(name = "remain") var remain: Long = 0L,
    @ColumnInfo(name = "date") var date: Long = 0L,
    @ColumnInfo(name = "taskId") var taskId: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)