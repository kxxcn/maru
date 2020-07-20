package dev.kxxcn.maru.view.present.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.KEY_PRESENT_IMAGE_RES
import dev.kxxcn.maru.util.extension.displayWidth
import kotlinx.android.synthetic.main.description_pager_fragment.*

class DescriptionPagerFragment : Fragment() {

    private var imageRes: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.description_pager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupPagerImage()
    }

    override fun onStop() {
        context?.let { GlideApp.with(this).clear(desc_pager_image) }
        super.onStop()
    }

    private fun setupArguments() {
        imageRes = arguments?.getInt(KEY_PRESENT_IMAGE_RES)
    }

    private fun setupPagerImage() {
        GlideApp.with(this)
            .load(imageRes)
            .centerCrop()
            .override(displayWidth() / 2)
            .into(desc_pager_image)
    }
}
