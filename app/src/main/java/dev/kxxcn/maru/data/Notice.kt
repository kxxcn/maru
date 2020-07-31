package dev.kxxcn.maru.data

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import dev.kxxcn.maru.util.DateUtils

@Keep
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
                DateUtils.DATE_FORMAT_2.format(date)
            }
        }
}
