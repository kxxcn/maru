package dev.kxxcn.maru.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.Purchase
import com.google.firebase.firestore.QuerySnapshot
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class FakeRepository : DataRepository {

    private var taskData: LinkedHashMap<String, Task> by Delegates.observable(LinkedHashMap()) { _, _, taskData ->
        updateSummary(taskData = taskData)
    }

    private val taskDetailsData: LinkedHashMap<String, TaskDetail> = LinkedHashMap()

    private var user: User by Delegates.observable(fetchUser()) { _, _, user ->
        updateSummary(user = user)
    }

    private var summaries = mutableListOf<Summary>()

    init {
        taskData = fetchTasks()
    }

    override suspend fun getUsers(): Result<List<User>> {
        return Success(listOf(user))
    }

    override suspend fun saveUser(user: User): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTasks(tasks: List<Task>): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTasks(tasks: List<Task>): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(taskId: String): Result<TaskDetail> {
        val taskDetail = taskDetailsData[taskId] ?: throw RuntimeException("Invalid Item.")
        return Success(taskDetail)
    }

    override suspend fun getAccounts(): Result<List<Account>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccount(taskId: String): Result<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAccount(account: Account): Result<Any?> {
        return Success(Any())
    }

    override suspend fun replaceTasks(items: List<Task>) {
        taskData.clear()
        taskData = linkedMapOf(*items.map { it.id to it }.toTypedArray())
    }

    override suspend fun getSummary(): Result<List<Summary>> {
        return Success(summaries)
    }

    override fun observeSummary(): LiveData<List<Summary>> {
        return MutableLiveData<List<Summary>>().apply { value = summaries }
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

    override suspend fun getNotices(): Result<QuerySnapshot?> {
        TODO("Not yet implemented")
    }

    override suspend fun editName(name: String): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun editBudget(budget: Long): Result<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun savePremium(email: String?, purchase: Purchase): Result<Any?> {
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

    @VisibleForTesting
    fun addTaskDetails(vararg details: TaskDetail) {
        for (taskDetail in details) {
            val id = taskDetail.task?.id ?: continue
            taskDetailsData[id] = taskDetail
        }
    }

    @VisibleForTesting
    fun replaceUser(newUser: User) {
        user = newUser
    }

    private fun fetchUser(): User {
        val wedding = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5)
        return User(name = "테스트", budget = 100000000L, wedding = wedding)
    }

    private fun fetchTasks(): LinkedHashMap<String, Task> {
        val task1 = Task(name = "Task 1")
        val task2 = Task(name = "Task 2")
        val task3 = Task(name = "Task 3")
        return linkedMapOf(task1.id to task1, task2.id to task2, task3.id to task3)
    }

    private fun updateSummary(user: User? = null, taskData: Map<String, Task>? = null) {
        summaries.clear()
        summaries.add(
            Summary().apply {
                this.user = user ?: fetchUser()
                this.tasks = (taskData ?: fetchTasks()).map {
                    TaskDetail().apply {
                        task = it.value
                        account = Account(
                            husband = 500000,
                            wife = 250000,
                            date = System.currentTimeMillis(),
                            taskId = it.value.id
                        )
                    }
                }
            }
        )
    }
}
