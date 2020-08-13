package dev.kxxcn.maru.util.preference

import dev.kxxcn.maru.util.extension.getBoolean
import dev.kxxcn.maru.util.extension.getInt
import dev.kxxcn.maru.util.extension.putBoolean
import dev.kxxcn.maru.util.extension.putInt

object PreferenceUtils {

    /**
     * 온보드 표시 여부
     */
    var shownOnboard: Boolean
        get() = "shownOnboard".getBoolean(false)
        set(value) = "shownOnboard".putBoolean(value)

    /**
     * 통화 단위
     */
    var unitType: Int
        get() = "unitType".getInt(0)
        set(value) = "unitType".putInt(value)

    /**
     * 체크리스트 뷰 타입
     */
    var taskFilterType: Int
        get() = "taskFilterType".getInt(0)
        set(value) = "taskFilterType".putInt(value)

    /**
     * 야간모드 활성화 여부
     */
    var useDarkMode: Boolean
        get() = "useDarkMode".getBoolean(false)
        set(value) = "useDarkMode".putBoolean(value)

    /**
     * 공지사항 알림
     */
    var notifyNotice: Boolean
        get() = "notifyNotice".getBoolean(true)
        set(value) = "notifyNotice".putBoolean(value)

    /**
     * 공지사항 알림 진동
     */
    var notifyNoticeVibrate: Boolean
        get() = "notifyNoticeVibrate".getBoolean(true)
        set(value) = "notifyNoticeVibrate".putBoolean(value)

    /**
     * 공지사항 알림 소리
     */
    var notifyNoticeSound: Boolean
        get() = "notifyNoticeSound".getBoolean(true)
        set(value) = "notifyNoticeSound".putBoolean(value)
}
