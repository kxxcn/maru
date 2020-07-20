package dev.kxxcn.maru.view.register

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import dev.kxxcn.maru.R

@BindingAdapter("app:visibility")
fun setVisibility(v: View, isShowing: Boolean) {
    val parent = v.parent as? MotionLayout ?: return
    val visibility = if (isShowing) View.VISIBLE else View.GONE
    for (id in parent.constraintSetIds) {
        val c = parent.getConstraintSet(id)
        c?.setVisibility(v.id, visibility)
    }
}

@BindingAdapter("app:infoText")
fun setInfoText(v: EditText, value: String?) {
    val oldText = v.text.toString()
    if (oldText == value || (value == null && oldText.isEmpty())) {
        return
    }
    value?.let {
        v.text = SpannableStringBuilder(it)
        v.setSelection(it.length)
    }
}

@InverseBindingAdapter(attribute = "app:infoText")
fun getInfoText(v: EditText): String {
    return v.text.toString()
}

@BindingAdapter("app:infoTextAttrChanged")
fun setInfoTextListener(v: EditText, infoTextAttrChanged: InverseBindingListener?) {
    val newValue = v.doOnTextChanged { _, _, _, _ ->
        infoTextAttrChanged?.onChange()
    }
    val oldValue = ListenerUtil.trackListener(v, newValue, R.id.textWatcher)
    oldValue?.let {
        v.removeTextChangedListener(it)
    }
    newValue.let {
        v.addTextChangedListener(it)
    }
}
