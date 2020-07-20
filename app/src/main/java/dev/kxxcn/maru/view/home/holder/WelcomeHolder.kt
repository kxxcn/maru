package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.gelitenight.waveview.library.WaveView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.WelcomeItemBinding
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.WaveHelper
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeAdapter

class WelcomeHolder(
    private val binding: WelcomeItemBinding
) : LifecycleViewHolder(binding) {

    private var waveHelper: WaveHelper? = null

    fun bind(item: HomeAdapter.SummaryItem): () -> Unit {
        with(binding) {
            with(welcomeWave) {
                setWaveColor(
                    ContextCompat.getColor(context, R.color.waveBehind),
                    ContextCompat.getColor(context, R.color.waveFront)
                )
                setShapeType(WaveView.ShapeType.CIRCLE)
                val percentage = ConvertUtils.computeProgress(item.content.user?.wedding)
                waveHelper = WaveHelper(this, percentage).also { it.start() }
            }
            this.lifecycleOwner = this@WelcomeHolder
            this.content = item.content
            this.executePendingBindings()
        }
        return { release() }
    }

    fun release() {
        waveHelper?.stop()
        waveHelper = null
    }

    companion object {

        fun from(parent: ViewGroup): WelcomeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = WelcomeItemBinding.inflate(layoutInflater, parent, false)
            return WelcomeHolder(binding)
        }
    }
}
