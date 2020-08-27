package dev.kxxcn.maru.util

import dev.kxxcn.maru.view.days.DaysAddFragment
import dev.kxxcn.maru.view.days.DaysFragment
import dev.kxxcn.maru.view.edit.EditDialogFragment
import dev.kxxcn.maru.view.edit.EditFragment
import dev.kxxcn.maru.view.home.HomeFragment
import dev.kxxcn.maru.view.input.InputFragment
import dev.kxxcn.maru.view.landmark.LandmarkFragment
import dev.kxxcn.maru.view.more.MoreFragment
import dev.kxxcn.maru.view.notice.NoticeFragment
import dev.kxxcn.maru.view.notification.NotificationFragment
import dev.kxxcn.maru.view.order.OrderFragment
import dev.kxxcn.maru.view.present.PresentFragment
import dev.kxxcn.maru.view.purchase.PurchaseFragment
import dev.kxxcn.maru.view.setting.SettingFragment
import dev.kxxcn.maru.view.sort.SortFragment
import dev.kxxcn.maru.view.status.StatusFragment
import dev.kxxcn.maru.view.tasks.TasksFragment
import dev.kxxcn.maru.view.timeline.TimelineFragment

object AnalyticsUtils {

    private val SCREEN_MAP = mapOf(
        HomeFragment::class.java.name to "홈 화면",
        TasksFragment::class.java.name to "체크리스트 화면",
        MoreFragment::class.java.name to "더보기 화면",
        DaysFragment::class.java.name to "디데이 목록 화면",
        DaysAddFragment::class.java.name to "디데이 입력 화면",
        EditFragment::class.java.name to "프로필 정보 화면",
        EditDialogFragment::class.java.name to "프로필 편집 화면",
        InputFragment::class.java.name to "체크리스트 입력/수정 화면",
        StatusFragment::class.java.name to "체크리스트 상태 화면",
        SortFragment::class.java.name to "체크리스트 편집 화면",
        LandmarkFragment::class.java.name to "셀프웨딩 화면",
        NoticeFragment::class.java.name to "공지사항 화면",
        NotificationFragment::class.java.name to "알림 설정 화면",
        OrderFragment::class.java.name to "결혼식 순서 화면",
        PresentFragment::class.java.name to "예물/예단 화면",
        PurchaseFragment::class.java.name to "프리미엄 구매 화면",
        SettingFragment::class.java.name to "설정 화면",
        TimelineFragment::class.java.name to "타임라인 화면"
    )

    fun screenName(key: String): String? {
        return SCREEN_MAP[key]
    }
}
