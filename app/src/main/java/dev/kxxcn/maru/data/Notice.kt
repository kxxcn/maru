package dev.kxxcn.maru.data

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Notice(
    val subject: String = "",
    val content: String = "",
    val createdAt: Timestamp? = null
) {

    val date: String
        get() {
            val date = createdAt?.toDate()
            return if (date == null) {
                "시간 정보가 없습니다."
            } else {
                SimpleDateFormat("yyyy년 MM월 dd일 HH:MM:ss", Locale.getDefault()).format(date)
            }
        }
}
