package dev.kxxcn.maru.util.extension

import dev.kxxcn.maru.util.preference.PreferenceProvider

fun String.putString(value: String?) {
    PreferenceProvider.instance.edit().putString(this, value).apply()
}

fun String.putInt(value: Int) {
    PreferenceProvider.instance.edit().putInt(this, value).apply()
}

fun String.putLong(value: Long) {
    PreferenceProvider.instance.edit().putLong(this, value).apply()
}

fun String.putBoolean(value: Boolean) {
    PreferenceProvider.instance.edit().putBoolean(this, value).apply()
}

fun String.getString(defValue: String?): String? {
    return PreferenceProvider.instance.getString(this, defValue)
}

fun String.getInt(defValue: Int): Int {
    return PreferenceProvider.instance.getInt(this, defValue)
}

fun String.getLong(defValue: Long): Long {
    return PreferenceProvider.instance.getLong(this, defValue)
}

fun String.getBoolean(defValue: Boolean): Boolean {
    return PreferenceProvider.instance.getBoolean(this, defValue)
}
