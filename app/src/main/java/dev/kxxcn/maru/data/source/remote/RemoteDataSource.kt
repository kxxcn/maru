package dev.kxxcn.maru.data.source.remote

import androidx.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.Result.Error
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataSource
import dev.kxxcn.maru.data.source.api.NaverService
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteDataSource(
    private val naverService: NaverService,
    private val ioDispatcher: CoroutineDispatcher
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

    override suspend fun getSummary(): Result<List<Summary>> {
        TODO("Not yet implemented")
    }

    override fun observeSummary(): LiveData<List<Summary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDirection(
        start: String,
        goal: String,
        option: String?
    ): Result<DirectionDto?> = withContext(ioDispatcher) {
        return@withContext try {
            Success(naverService.getDirections(start, goal, option))
        } catch (e: Exception) {
            Error(e)
        }
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

    override suspend fun getNotices(): Result<QuerySnapshot?> {
        TODO("Not yet implemented")
    }

    override suspend fun editName(name: String): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun editBudget(budget: Long): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: Task): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun savePremium(email: String?, purchase: Purchase?): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun isPremium(email: String?): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun backup(email: String, encoded: String): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun findRestore(email: String): Result<Restore?> {
        TODO("Not yet implemented")
    }

    override suspend fun restore(summary: Summary): Result<Any?> {
        TODO("Not yet implemented")
    }
}
