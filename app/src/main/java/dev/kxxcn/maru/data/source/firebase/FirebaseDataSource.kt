package dev.kxxcn.maru.data.source.firebase

import androidx.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import dev.kxxcn.maru.data.*
import dev.kxxcn.maru.data.Result.Error
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataSource
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import dev.kxxcn.maru.util.*
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
                .collection(COLLECTION_NOTICE)
                .orderBy(FILED_CREATED_AT, DESCENDING)
                .get()
                .await()
            Success(data)
        } catch (e: Exception) {
            Error(e)
        }
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

    override suspend fun savePremium(email: String?, purchase: Purchase?): Result<Any?> =
        withContext(ioDispatcher) {
            return@withContext try {
                if (purchase == null) throw NullPointerException("Invalid input object.")
                val history = hashMapOf(
                    FILED_EMAIL to email,
                    FILED_ORDER_ID to purchase.orderId,
                    FILED_ORDER_TIME to purchase.purchaseTime,
                    FILED_ORDER_TOKEN to purchase.purchaseToken,
                    FILED_SIGNATURE to purchase.signature,
                    FILED_SKU to purchase.skus.toString()
                )
                val data = firestore
                    .collection(COLLECTION_PURCHASE)
                    .add(history)
                    .await()
                Success(data)
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun isPremium(email: String?): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            if (email == null) throw NullPointerException("Invalid email address.")
            val data = firestore
                .collection(COLLECTION_PURCHASE)
                .whereEqualTo(FILED_EMAIL, email)
                .get()
                .await()
            Success(!data.isEmpty)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun backup(email: String, encoded: String): Result<Any?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val backup = hashMapOf(
                    FILED_DATA to encoded,
                    FILED_TIME to System.currentTimeMillis()
                )
                firestore
                    .collection(COLLECTION_BACKUP)
                    .document(email)
                    .set(backup)
                    .await()
                Success(Unit)
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun findRestore(email: String): Result<Restore?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val data = firestore
                    .collection(COLLECTION_BACKUP)
                    .document(email)
                    .get()
                    .await()
                    .run { toObject<Restore>() }
                Success(data)
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun restore(summary: Summary): Result<Any?> {
        TODO("Not yet implemented")
    }
}
