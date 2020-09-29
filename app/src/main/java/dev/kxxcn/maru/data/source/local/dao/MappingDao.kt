package dev.kxxcn.maru.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.TaskDetail

@Dao
interface MappingDao {

    @Transaction
    @Query("SELECT * FROM Users")
    fun observeSummaries(): LiveData<List<Summary>>

    @Transaction
    @Query("SELECT * FROM Users")
    fun getSummaries(): List<Summary>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskDetail(taskId: String): TaskDetail
}
