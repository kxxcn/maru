package dev.kxxcn.maru.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

object AttrsUtils {

    @JvmStatic
    fun getColor(context: Context, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
