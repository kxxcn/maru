package dev.kxxcn.maru.view.notification

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.zcw.togglebutton.ToggleButton

@BindingAdapter("app:toggle")
fun setToggle(view: ToggleButton, isOn: Boolean) {
    if (isOn) {
        view.setToggleOn()
    } else {
        view.setToggleOff()
    }
}

@InverseBindingAdapter(attribute = "app:toggle", event = "app:toggleAttrChanged")
fun getToggle(view: ToggleButton): Boolean {
    val toggleOn = ToggleButton::class.java
        .getDeclaredField("toggleOn")
        .apply { isAccessible = true }
    return toggleOn.get(view) as? Boolean ?: false
}

@BindingAdapter("app:toggleAttrChanged")
fun setToggleListener(view: ToggleButton, listener: InverseBindingListener?) {
    view.setOnToggleChanged {
        listener?.onChange()
    }
}
