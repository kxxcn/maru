package dev.kxxcn.maru.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.gelitenight.waveview.library.WaveView


class WaveHelper(
    private val view: WaveView,
    private val percentage: Float
) {

    private var animatorSet: AnimatorSet? = null

    init {
        initAnimation()
    }

    fun start() {
        view.isShowWave = true
        animatorSet?.start()
    }

    fun stop() {
        view.isShowWave = false
        animatorSet?.pause()
        animatorSet?.removeAllListeners()
        animatorSet = null
    }

    private fun initAnimation() {
        val animators: MutableList<Animator> = ArrayList()

        ObjectAnimator.ofFloat(
            view,
            "waveShiftRatio",
            0f,
            1f
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 700
            interpolator = LinearInterpolator()
        }.also { animators.add(it) }

        ObjectAnimator.ofFloat(
            view,
            "waterLevelRatio",
            0f,
            percentage
        ).apply {
            duration = 10000
            interpolator = DecelerateInterpolator()
        }.also { animators.add(it) }

        ObjectAnimator.ofFloat(
            view,
            "amplitudeRatio",
            0.0001f,
            0.05f
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 5000
            interpolator = LinearInterpolator()
        }.also { animators.add(it) }

        animatorSet = AnimatorSet().apply { playTogether(animators) }
    }
}
