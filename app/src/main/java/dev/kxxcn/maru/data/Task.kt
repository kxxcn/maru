package dev.kxxcn.maru.data

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Task(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "priority") var priority: Long = 0L,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "iconId") var iconId: String = "",
    @ColumnInfo(name = "userId") var userId: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {
    companion object {

        fun createTasks(items: List<Task>, userId: String): List<Task> {
            return mutableListOf<Task>().also { tasks ->
                for ((index, task) in items.withIndex()) {
                    tasks.add(
                        Task(
                            name = task.name,
                            priority = index.toLong(),
                            iconId = task.iconId,
                            userId = userId
                        )
                    )
                }
            }
        }
    }
}

class TaskDetail {

    @Embedded
    var task: Task? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
        entity = Account::class
    )
    var account: Account? = null

    val totalAccounts: Long
        get() {
            val husbandAccount = account?.husband ?: 0
            val wifeAccount = account?.wife ?: 0
            return husbandAccount + wifeAccount
        }

    val husbandProgress: Float
        get() {
            return if (totalAccounts == 0L) {
                0f
            } else {
                val husband = account?.husband ?: 0
                val remain = account?.remain ?: 0
                (husband.toDouble() / (totalAccounts.toDouble() + remain.toDouble())).toFloat()
            }
        }

    val wifeProgress: Float
        get() {
            return if (totalAccounts == 0L) {
                0f
            } else {
                val wife = account?.wife ?: 0
                val remain = account?.remain ?: 0
                (wife.toDouble() / (totalAccounts.toDouble() + remain.toDouble())).toFloat()
            }
        }

    val accountDate: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            return dateFormat.format(account?.date)
        }

    override fun equals(other: Any?): Boolean {
        if (other !is TaskDetail) return false
        return task == other.task
    }

    override fun hashCode(): Int {
        return task.hashCode()
    }
}
