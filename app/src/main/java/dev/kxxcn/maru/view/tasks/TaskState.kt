package dev.kxxcn.maru.view.tasks

/**
 * 체크리스트 행의 상태. 정렬 순서는 ordinal 순(진행 중, 준비 전, 완료).
 */
enum class TaskState {
    /** 미완료이고 지출 기록이 있다 */
    ACTIVE,
    /** 미완료이고 지출 기록이 없다 */
    READY,
    /** 완료 */
    DONE
}
