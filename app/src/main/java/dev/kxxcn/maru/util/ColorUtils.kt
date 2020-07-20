package dev.kxxcn.maru.util

import dev.kxxcn.maru.R

object ColorUtils {

    fun getDaysColor(): Int {
        return listOf(
            R.color.day_background0,
            R.color.day_background1,
            R.color.day_background2,
            R.color.day_background3,
            R.color.day_background4,
            R.color.day_background5,
            R.color.day_background6,
            R.color.day_background7,
            R.color.day_background8,
            R.color.day_background9,
            R.color.day_background10,
            R.color.day_background11,
            R.color.day_background12,
            R.color.day_background13,
            R.color.day_background14,
            R.color.day_background15,
            R.color.day_background16,
            R.color.day_background17
        ).random()
    }
}
