package dev.kxxcn.maru.util.extension

import dev.kxxcn.maru.util.preference.PreferenceManager

fun String.putString(value: String?) {
    PreferenceManager.putString(this, value)
}

fun String.putInt(value: Int) {
    PreferenceManager.putInt(this, value)
}

fun String.putLong(value: Long) {
    PreferenceManager.putLong(this, value)
}

fun String.putBoolean(value: Boolean) {
    PreferenceManager.putBoolean(this, value)
}

fun String.getString(defValue: String?): String? {
    return PreferenceManager.getString(this, defValue)
}

fun String.getInt(defValue: Int): Int {
    return PreferenceManager.getInt(this, defValue)
}

fun String.getLong(defValue: Long): Long {
    return PreferenceManager.getLong(this, defValue)
}

fun String.getBoolean(defValue: Boolean): Boolean {
    return PreferenceManager.getBoolean(this, defValue)
}
