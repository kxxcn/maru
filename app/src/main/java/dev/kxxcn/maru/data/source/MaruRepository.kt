package dev.kxxcn.maru.data.source

import androidx.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import dev.kxxcn.maru.di.ApplicationModule.FirebaseDataSource
import dev.kxxcn.maru.di.ApplicationModule.LocalDataSource
import dev.kxxcn.maru.di.ApplicationModule.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MaruRepository @Inject constructor(
    @LocalDataSource private val localDataSource: DataSource,
    @RemoteDataSource private val remoteDataSource: DataSource,
    @FirebaseDataSource private val firebaseDataSource: DataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return localDataSource.getUsers()
    }

    override suspend fun saveUser(user: User): Result<Any?> {
        return localDataSource.saveUser(user)
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return localDataSource.getTasks()
    }

    override suspend fun updateTasks(tasks: List<Task>): Result<Any?> {
        return localDataSource.updateTasks(tasks)
    }

    override suspend fun deleteTasks(tasks: List<Task>): Result<Any?> {
        return localDataSource.deleteTasks(tasks)
    }

    override suspend fun getAccounts(): Result<List<Account>> {
        return localDataSource.getAccounts()
    }

    override suspend fun getAccount(taskId: String): Result<Account> {
        return localDataSource.getAccount(taskId)
    }

    override suspend fun saveAccount(account: Account): Result<Any?> {
        return localDataSource.saveAccount(account)
    }

    override suspend fun getSummary(): Result<List<Summary>> {
        return localDataSource.getSummary()
    }

    override fun observeSummary(): LiveData<List<Summary>> {
        return localDataSource.observeSummary()
    }

    override suspend fun replaceTasks(items: List<Task>) {
        return localDataSource.replaceTasks(items)
    }

    override suspend fun getTaskDetail(taskId: String): Result<TaskDetail> {
        return localDataSource.getTaskDetail(taskId)
    }

    override suspend fun getDirection(
        start: String,
        goal: String,
        option: String?
    ): Result<DirectionDto?> {
        return localDataSource.getDirection(start, goal, option)
            .takeIf { it.succeeded }
            ?: remoteDataSource.getDirection(start, goal, option)
                .also {
                    if (it is Success && it.data?.code == 0) {
                        saveDirection(it.data, goal)
                    }
                }
    }

    override suspend fun saveDirection(direction: DirectionDto, goal: String): Result<Any?> {
        return localDataSource.saveDirection(direction, goal)
    }

    override suspend fun saveDay(day: Day): Result<Any?> {
        return localDataSource.saveDay(day)
    }

    override suspend fun deleteDay(day: Day): Result<Any?> {
        return localDataSource.deleteDay(day)
    }

    override suspend fun getNotices(): Result<QuerySnapshot?> {
        return firebaseDataSource.getNotices()
    }

    override suspend fun editName(name: String): Result<Any?> {
        return localDataSource.editName(name)
    }

    override suspend fun editBudget(budget: Long): Result<Any?> {
        return localDataSource.editBudget(budget)
    }

    override suspend fun addTask(task: Task): Result<Any?> {
        return localDataSource.addTask(task)
    }

    override suspend fun savePremium(email: String?, purchase: Purchase): Result<Any?> {
        return localDataSource.savePremium(purchase = purchase).also {
            if (it.succeeded) {
                firebaseDataSource.savePremium(email, purchase)
            }
        }
    }

    override suspend fun isPremium(email: String?): Result<Boolean> {
        return localDataSource.isPremium(email)
            .takeIf { it is Success && it.data }
            ?: firebaseDataSource.isPremium(email).also {
                if (it is Success && it.data) {
                    localDataSource.savePremium()
                }
            }
    }

    override suspend fun backup(email: String, encoded: String): Result<Any?> {
        return firebaseDataSource.backup(email, encoded)
    }

    override suspend fun findRestore(email: String): Result<Restore?> {
        return firebaseDataSource.findRestore(email)
    }

    override suspend fun restore(summary: Summary): Result<Any?> {
        return localDataSource.restore(summary)
    }
}
