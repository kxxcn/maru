package dev.kxxcn.maru.data.source.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.source.DataSource
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import dev.kxxcn.maru.util.COLLECTION_NOTICES
import dev.kxxcn.maru.util.FILED_CREATED_AT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseDataSource(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override suspend fun getUsers(): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: User): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, isCompleted: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTasks(tasks: List<Task>): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTasks(tasks: List<Task>): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(taskId: String): Result<TaskDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccounts(): Result<List<Account>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccount(taskId: String): Result<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAccount(account: Account): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun replaceTasks(items: List<Task>) {
        TODO("Not yet implemented")
    }

    override fun observeSummary(): LiveData<List<Summary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDirection(
        start: String,
        goal: String,
        option: String?
    ): Result<DirectionDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDirection(direction: DirectionDto, goal: String): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDay(day: Day): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDay(day: Day): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotices(): Result<QuerySnapshot?> = withContext(ioDispatcher) {
        return@withContext try {
            val data = firestore
                .collection(COLLECTION_NOTICES)
                .orderBy(FILED_CREATED_AT, DESCENDING)
                .get()
                .await()
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun editName(name: String): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun editBudget(budget: Long): Result<Any?> {
        TODO("Not yet implemented")
    }
}
