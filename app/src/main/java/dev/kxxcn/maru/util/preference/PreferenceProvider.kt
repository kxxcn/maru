package dev.kxxcn.maru.util.preference

import android.content.Context
import android.content.SharedPreferences
import dev.kxxcn.maru.R

object PreferenceProvider {

    lateinit var instance: SharedPreferences

    fun createInstance(context: Context) {
        instance = context.getSharedPreferences(
            context.getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        )
    }
}
