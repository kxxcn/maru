package dev.kxxcn.maru.data

import androidx.room.Embedded
import androidx.room.Relation
import java.text.NumberFormat
import java.util.*

class Summary {

    @Embedded
    var user: User? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Task::class
    )
    var tasks: List<TaskDetail> = emptyList()

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Day::class
    )
    var days: List<Day> = emptyList()

    private val accounts: List<Account?>
        get() = tasks.map { it.account }

    val totalTasksCount: Int
        get() = tasks.count()

    val completedTasksCount: Int
        get() = tasks.count { it.task?.isCompleted ?: false }

    val progressTasksCount: Int
        get() = tasks.count { !(it.task?.isCompleted ?: false) }

    val totalAccounts: Long
        get() = accounts.sumBy { ((it?.husband ?: 0L) + (it?.wife ?: 0L)).toInt() }.toLong()

    val totalAccountsText: String
        get() {
            val numberFormat = NumberFormat.getInstance(Locale.KOREA)
            return numberFormat.format(totalAccounts)
        }

    private val husbandAccounts: Long
        get() = accounts.sumBy { it?.husband?.toInt() ?: 0 }.toLong()

    private val wifeAccounts: Long
        get() = accounts.sumBy { it?.wife?.toInt() ?: 0 }.toLong()

    val husbandProgress: Float
        get() = if (totalAccounts == 0L) 0f else (husbandAccounts.toDouble() / totalAccounts.toDouble()).toFloat()

    val wifeProgress: Float
        get() = if (totalAccounts == 0L) 0f else (wifeAccounts.toDouble() / totalAccounts.toDouble()).toFloat()

    val taskProgress: Float
        get() = ((completedTasksCount.toDouble() / totalTasksCount.toDouble()) * 100).toFloat()

    val completedTasks: List<TaskDetail>
        get() = tasks.filter { it.task?.isCompleted == true }

    val progressTaskName: String?
        get() = tasks
            .filter { it.task?.isCompleted == false && it.account != null }
            .minBy { it.task?.priority ?: Long.MAX_VALUE }?.task?.name

    val progressTaskIcon: String?
        get() = tasks
            .filter { it.task?.isCompleted == false && it.account != null }
            .minBy { it.task?.priority ?: Long.MAX_VALUE }?.task?.iconId

    val monthAccounts: Long
        get() {
            if (accounts.isEmpty()) {
                return 0L
            } else {
                return accounts.filter {
                    val today = Calendar.getInstance()
                    val year = today.get(Calendar.YEAR)
                    val month = today.get(Calendar.MONTH)

                    val start = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    val end = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH))
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 0)
                    }
                    (start.timeInMillis <= it?.date ?: 0L) && (end.timeInMillis >= it?.date ?: 0L)
                }.sumBy { ((it?.husband ?: 0L) + (it?.wife ?: 0L)).toInt() }.toLong()
            }
        }

    val remainAccounts: Long
        get() {
            return if (accounts.isEmpty()) {
                0L
            } else {
                accounts.sumBy { it?.remain?.toInt() ?: 0 }.toLong()
            }
        }

    override fun equals(other: Any?): Boolean {
        if (other !is Summary) return false
        return tasks == other.tasks && days == other.days
    }

    override fun hashCode(): Int {
        return tasks.hashCode() + days.hashCode()
    }
}
