package dev.kxxcn.maru.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "budget") var budget: Long = 0L,
    @ColumnInfo(name = "wedding") var wedding: Long = 0L,
    @ColumnInfo(name = "premium") var premium: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)
