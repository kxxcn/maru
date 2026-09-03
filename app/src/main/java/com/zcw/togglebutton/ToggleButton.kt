package com.zcw.togglebutton

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat

class ToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SwitchCompat(context, attrs, defStyleAttr) {

    private var toggleChangedListener: ((Boolean) -> Unit)? = null

    var toggleOn: Boolean = false
        private set

    override fun setChecked(checked: Boolean) {
        val changed = toggleOn != checked
        super.setChecked(checked)
        toggleOn = checked
        if (changed) {
            toggleChangedListener?.invoke(checked)
        }
    }

    fun setToggleOn() {
        isChecked = true
    }

    fun setToggleOff() {
        isChecked = false
    }

    fun setOnToggleChanged(listener: (Boolean) -> Unit) {
        toggleChangedListener = listener
    }
}
