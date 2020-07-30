package dev.kxxcn.maru.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
object DateUtils {

    val DATE_FORMAT_1 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    val DATE_FORMAT_2 = SimpleDateFormat("yyyy년 MM월 dd일 HH:MM:ss", Locale.getDefault())

    val DATE_FORMAT_3 = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    val DATE_FORMAT_4 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val DATE_FORMAT_5 = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
}
