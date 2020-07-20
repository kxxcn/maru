package dev.kxxcn.maru.data.source.local

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.Result.Error
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataSource
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import dev.kxxcn.maru.data.source.local.dao.*
import dev.kxxcn.maru.util.COMPLETED_TASK
import dev.kxxcn.maru.util.UNCOMPLETED_TASK
import dev.kxxcn.maru.util.extension.coordinates
import dev.kxxcn.maru.util.extension.fromJson
import dev.kxxcn.maru.util.extension.toJson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val userDao: UserDao,
    private val taskDao: TaskDao,
    private val accountDao: AccountDao,
    private val mappingDao: MappingDao,
    private val directionDao: DirectionDao,
    private val dayDao: DayDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override suspend fun getUsers(): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(userDao.getUsers())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Any?> {
        return try {
            Success(userDao.insertUser(user))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(taskDao.getTasks())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun updateTask(taskId: String, isCompleted: Int) {
        taskDao.updateTask(taskId, isCompleted)
    }

    override suspend fun updateTasks(tasks: List<Task>): Result<Any?> {
        return try {
            Success(taskDao.updateTask(*tasks.toTypedArray()))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun deleteTasks(tasks: List<Task>): Result<Any?> {
        return try {
            Success(taskDao.deleteTask(*tasks.toTypedArray()))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getAccounts(): Result<List<Account>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(accountDao.getAccounts())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getAccount(taskId: String): Result<Account> = withContext(ioDispatcher) {
        return@withContext try {
            Success(accountDao.getAccount(taskId))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveAccount(account: Account): Result<Any?> = withContext(ioDispatcher) {
        return@withContext try {
            val isCompleted = if (account.remain == 0L) COMPLETED_TASK else UNCOMPLETED_TASK
            updateTask(account.taskId, isCompleted)
            Success(accountDao.insertAccount(account))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override fun observeSummary(): LiveData<List<Summary>> {
        return mappingDao.observeSummaries()
    }

    override suspend fun replaceTasks(items: List<Task>) {
        try {
            val user = userDao.getUsers().first()
            Success(taskDao.replaceTasks(Task.createTasks(items, user.id)))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getTaskDetail(taskId: String): Result<TaskDetail> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(mappingDao.getTaskDetail(taskId))
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getDirection(
        start: String,
        goal: String,
        option: String?
    ): Result<DirectionDto> = withContext(ioDispatcher) {
        return@withContext try {
            val (longitude, latitude) = goal.coordinates()
            val json = directionDao.getDirection(longitude, latitude)
            Success(json.fromJson<DirectionDto>())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveDirection(direction: DirectionDto, goal: String): Result<Any?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val json = direction.toJson()
                val (longitude, latitude) = goal.coordinates()
                Success(directionDao.insertDirection(Direction(json, longitude, latitude)))
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun saveDay(day: Day): Result<Any?> = withContext(ioDispatcher) {
        return@withContext try {
            val user = userDao.getUsers().first()
            day.userId = user.id
            Success(dayDao.insertDay(day))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun deleteDay(day: Day): Result<Any?> {
        return try {
            Success(dayDao.deleteDay(day))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getNotices(): Result<QuerySnapshot?> {
        TODO("Not yet implemented")
    }

    override suspend fun editName(name: String): Result<Any?> = withContext(ioDispatcher) {
        return@withContext try {
            userDao.getUsers()
                .first()
                .apply { this.name = name }
                .run { Success(userDao.updateUser(this)) }
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun editBudget(budget: Long): Result<Any?> = withContext(ioDispatcher) {
        return@withContext try {
            userDao.getUsers()
                .first()
                .apply { this.budget = budget }
                .run { Success(userDao.updateUser(this)) }
        } catch (e: Exception) {
            Error(e)
        }
    }
}
