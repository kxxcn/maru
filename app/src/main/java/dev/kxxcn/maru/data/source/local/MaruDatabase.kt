package dev.kxxcn.maru.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.source.local.dao.*
import dev.kxxcn.maru.view.days.DaysTypeConverter

@Database(
    entities = [
        User::class,
        Task::class,
        Account::class,
        Direction::class,
        Day::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DaysTypeConverter::class)
abstract class MaruDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao

    abstract fun accountDao(): AccountDao

    abstract fun mappingDao(): MappingDao

    abstract fun directionDao(): DirectionDao

    abstract fun dayDao(): DayDao
}
