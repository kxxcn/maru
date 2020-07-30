package dev.kxxcn.maru.util.extension

import dev.kxxcn.maru.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.year() = get(Calendar.YEAR)

fun Calendar.month() = get(Calendar.MONTH)

fun Calendar.day() = get(Calendar.DAY_OF_MONTH)

fun Long.msToDate(
    dateFormat: SimpleDateFormat = DateUtils.DATE_FORMAT_3
): String {
    val c = Calendar.getInstance().apply { timeInMillis = this@msToDate }
    val date = Date(c.timeInMillis)
    return dateFormat.format(date)
}
