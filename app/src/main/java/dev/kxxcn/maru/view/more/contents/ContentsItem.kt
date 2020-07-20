package dev.kxxcn.maru.view.more.contents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.kxxcn.maru.R

sealed class ContentsItem(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int
) {

    object AD : ContentsItem(R.drawable.ic_contents_ad, R.string.more_contents_ad)
    object BACKUP : ContentsItem(R.drawable.ic_contents_backup, R.string.more_contents_backup)
    object NIGHT : ContentsItem(R.drawable.ic_contents_night, R.string.more_contents_night)
    object REVIEW : ContentsItem(R.drawable.ic_contents_review, R.string.more_contents_review)
    object DAYS : ContentsItem(R.drawable.ic_contents_event, R.string.more_contents_event)
    object TIMELINE : ContentsItem(R.drawable.ic_contents_timeline, R.string.more_contents_timeline)
    object ORDER : ContentsItem(R.drawable.ic_contents_order, R.string.more_contents_order)
    object RING : ContentsItem(R.drawable.ic_contents_ring, R.string.more_contents_ring)
    object DRESS : ContentsItem(R.drawable.ic_contents_dress, R.string.more_contents_dress)
    object TUXEDO : ContentsItem(R.drawable.ic_contents_tuxedo, R.string.more_contents_tuxedo)
    object HANBOK : ContentsItem(R.drawable.ic_contents_hanbok, R.string.more_contents_hanbok)
    object LANDMARK : ContentsItem(R.drawable.ic_contents_landmark, R.string.more_contents_landmark)
}
