package dev.kxxcn.maru.data.source

import androidx.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.source.api.dto.DirectionDto

interface DataRepository {

    suspend fun getUsers(): Result<List<User>>

    suspend fun saveUser(user: User): Result<Any?>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun updateTasks(tasks: List<Task>): Result<Any?>

    suspend fun deleteTasks(tasks: List<Task>): Result<Any?>

    suspend fun getTaskDetail(taskId: String): Result<TaskDetail>

    suspend fun getAccounts(): Result<List<Account>>

    suspend fun getAccount(taskId: String): Result<Account>

    suspend fun saveAccount(account: Account): Result<Any?>

    suspend fun replaceTasks(items: List<Task>)

    suspend fun getSummary(): Result<List<Summary>>

    fun observeSummary(): LiveData<List<Summary>>

    suspend fun getDirection(
        start: String,
        goal: String,
        option: String? = null
    ): Result<DirectionDto?>

    suspend fun saveDirection(direction: DirectionDto, goal: String): Result<Any?>

    suspend fun saveDay(day: Day): Result<Any?>

    suspend fun deleteDay(day: Day): Result<Any?>

    suspend fun getNotices(): Result<QuerySnapshot?>

    suspend fun editName(name: String): Result<Any?>

    suspend fun editBudget(budget: Long): Result<Any?>

    suspend fun addTask(task: Task): Result<Any?>

    suspend fun savePremium(email: String?, purchase: Purchase): Result<Any?>

    suspend fun isPremium(email: String?): Result<Boolean>

    suspend fun backup(email: String, encoded: String): Result<Any?>

    suspend fun findRestore(email: String): Result<Restore?>

    suspend fun restore(summary: Summary): Result<Any?>
}
