package dev.kxxcn.maru.view.onboard

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("app:pagerPosition")
fun setPagerPosition(view: ViewPager, position: Int) {
    view.currentItem = position
}

@InverseBindingAdapter(attribute = "app:pagerPosition", event = "app:pagerAttrChanged")
fun getPagerPosition(view: ViewPager): Int {
    return view.currentItem
}

@BindingAdapter("app:pagerAttrChanged")
fun setPagerListener(view: ViewPager, listener: InverseBindingListener?) {
    view.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            listener?.onChange()
        }
    })
}

@BindingAdapter("app:lottie")
fun setLottie(view: LottieAnimationView, fileName: String) {
    view.setAnimation(fileName)
}
