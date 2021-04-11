package dev.kxxcn.maru.data.source.local.dao

import androidx.room.*
import dev.kxxcn.maru.data.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM Tasks")
    suspend fun getTasks(): List<Task>

    @Update
    suspend fun updateTask(vararg task: Task): Int

    @Delete
    suspend fun deleteTask(vararg task: Task): Int

    @Query("UPDATE Tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTask(taskId: String, isCompleted: Int)

    @Query("DELETE FROM Tasks")
    suspend fun deleteTaskAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAll(tasks: List<Task>)

    @Transaction
    suspend fun replaceTasks(tasks: List<Task>) {
        deleteTaskAll()
        insertTaskAll(tasks)
    }
}
