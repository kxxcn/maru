package dev.kxxcn.maru.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.kxxcn.maru.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM Users")
    fun observeUsers(): LiveData<List<User>>

    @Query("SELECT * FROM Users")
    suspend fun getUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User): Int
}
