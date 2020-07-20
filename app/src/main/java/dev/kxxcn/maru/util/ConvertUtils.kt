package dev.kxxcn.maru.util

import androidx.databinding.InverseMethod
import dev.kxxcn.maru.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ConvertUtils {

    fun moneyText(money: Long): String {
        if (money == 0L) return "0원"
        return buildString {
            val oneHundredsMillion = money / ONE_HUNDREDS_MILLION
            val tenThousand = (money - oneHundredsMillion * ONE_HUNDREDS_MILLION) / TEN_THOUSAND
            val other =
                (money - oneHundredsMillion * ONE_HUNDREDS_MILLION) - tenThousand * TEN_THOUSAND

            if (oneHundredsMillion != 0L) append("${oneHundredsMillion}억")
            if (tenThousand != 0L) append("${tenThousand}만")
            if (other != 0L) {
                if (tenThousand != 0L || money < 10000L) {
                    append(other)
                }
            }

            append("원")
        }
    }

    @InverseMethod("textFormat")
    fun moneyFormat(text: String?): String? {
        return try {
            val number = text
                ?.replace(",", "")
                ?.toInt()
            val decimalFormat = NumberFormat.getInstance(Locale.KOREA)
            decimalFormat.format(number)
        } catch (e: Exception) {
            text
        }
    }

    fun moneyFormat(number: Long?): String? {
        val decimalFormat = NumberFormat.getInstance(Locale.KOREA)
        return decimalFormat.format(number)
    }

    fun textFormat(number: String?): String? {
        return number?.replace(",", "") ?: number
    }

    @InverseMethod("inverseRemain")
    fun computeRemain(time: Long?): Long? {
        val remain = time?.minus(System.currentTimeMillis())
        return TimeUnit.MILLISECONDS.toDays(remain ?: 0L) + 1
    }

    fun inverseRemain(remain: Long?): Long? = 0L

    @InverseMethod("inverseProgress")
    fun computeProgress(time: Long?): Float {
        val remain = time?.minus(System.currentTimeMillis())
        val day = TimeUnit.MILLISECONDS.toDays(remain ?: 0L) + 1
        return when {
            day > 365 -> 0.1f
            day > 200 -> 0.2f
            day > 150 -> 0.4f
            day > 80 -> 0.6f
            day > 40 -> 0.8f
            day > 20 -> 0.9f
            else -> 1.0f
        }
    }

    fun inverseProgress(progress: Float): Long? = 0L

    @InverseMethod("inverseDateFormat")
    fun dateFormat(time: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun inverseDateFormat(time: String): Long? {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return dateFormat.parse(time)?.time
    }

    fun getRemain(selection: Long): Pair<Int, Int> {
        val today = Calendar.getInstance()
        val decimalDay = Calendar.getInstance().apply {
            timeInMillis = selection
        }
        val longDecimalDay = decimalDay.timeInMillis / (24 * 60 * 60 * 1000)
        val longToday = today.timeInMillis / (24 * 60 * 60 * 1000)
        val count = (longToday - 1 - longDecimalDay).toInt()
        return when {
            count > 0 -> R.string.days_add_calculate_remain_plus to count
            count == 0 -> R.string.days_add_calculate_remain_day to count
            else -> R.string.days_add_calculate_remain_minus to count
        }
    }

    fun getCount(selection: Long): Pair<Int, Int> {
        val today = Calendar.getInstance()
        val decimalDay = Calendar.getInstance().apply {
            timeInMillis = selection
        }
        val longDecimalDay = decimalDay.timeInMillis / (24 * 60 * 60 * 1000)
        val longToday = today.timeInMillis / (24 * 60 * 60 * 1000)
        val count = (longToday - longDecimalDay).toInt()
        return R.string.days_add_calculate_count to count
    }
}
