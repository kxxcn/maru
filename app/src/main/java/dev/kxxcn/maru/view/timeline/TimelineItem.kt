package dev.kxxcn.maru.view.timeline

import androidx.annotation.StringRes
import dev.kxxcn.maru.R

data class TimelineItem(
    val days: Int,
    val tasks: List<TimelineTask>
) {

    companion object {

        fun create(): List<TimelineItem> {
            return listOf(
                TimelineItem(
                    180,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_greeting_name,
                            R.string.timeline_item_greeting_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_hall_research_name,
                            R.string.timeline_item_hall_research_content
                        )
                    )

                ),

                TimelineItem(
                    150,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_marriage_budget_name,
                            R.string.timeline_item_marriage_budget_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_marriage_house_name,
                            R.string.timeline_item_marriage_house_content
                        )
                    )
                ),
                TimelineItem(
                    120,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_honeymoon_name,
                            R.string.timeline_item_honeymoon_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_dress_name,
                            R.string.timeline_item_dress_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_studio_name,
                            R.string.timeline_item_studio_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_makeup_name,
                            R.string.timeline_item_makeup_content
                        )
                    )
                ),
                TimelineItem(
                    70,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_skin_care_name,
                            R.string.timeline_item_skin_care_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_contract_house_name,
                            R.string.timeline_item_contract_house_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_gift_name,
                            R.string.timeline_item_gift_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_hanbok_name,
                            R.string.timeline_item_hanbok_content
                        )
                    )
                ),
                TimelineItem(
                    50,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_prepare_passport_name,
                            R.string.timeline_item_prepare_passport_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_photo_name,
                            R.string.timeline_item_photo_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_present_name,
                            R.string.timeline_item_present_content
                        )
                    )
                ),
                TimelineItem(
                    30,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_invitation_name,
                            R.string.timeline_item_invitation_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_furniture_name,
                            R.string.timeline_item_furniture_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_helper_name,
                            R.string.timeline_item_helper_content
                        )
                    )
                ),
                TimelineItem(
                    10,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_delivery_name,
                            R.string.timeline_item_delivery_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_purchase_honeymoon_name,
                            R.string.timeline_item_purchase_honeymoon_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_contribution_name,
                            R.string.timeline_item_contribution_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_interior_name,
                            R.string.timeline_item_interior_content
                        )
                    )
                ),
                TimelineItem(
                    7,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_repair_name,
                            R.string.timeline_item_repair_content
                        ),
                        TimelineTask(
                            R.string.timeline_item_rehearsal_name,
                            R.string.timeline_item_rehearsal_content
                        )
                    )
                ),
                TimelineItem(
                    3,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_prepare_honeymoon_name,
                            R.string.timeline_item_prepare_honeymoon_content
                        )
                    )
                ),
                TimelineItem(
                    0,
                    listOf(
                        TimelineTask(
                            R.string.timeline_item_day_name,
                            R.string.timeline_item_day_content
                        )
                    )
                )
            )
        }
    }
}

data class TimelineTask(
    @StringRes val nameRes: Int,
    @StringRes val contentRes: Int
)
