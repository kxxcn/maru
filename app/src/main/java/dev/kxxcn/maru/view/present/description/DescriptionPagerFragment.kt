package dev.kxxcn.maru.view.present.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.databinding.DescriptionPagerFragmentBinding
import dev.kxxcn.maru.util.KEY_PRESENT_IMAGE_RES
import dev.kxxcn.maru.util.extension.displayWidth

class DescriptionPagerFragment : Fragment() {

    private var _binding: DescriptionPagerFragmentBinding? = null
    private val binding get() = _binding!!

    private var imageRes: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DescriptionPagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupPagerImage()
    }

    override fun onStop() {
        _binding?.let { GlideApp.with(this).clear(it.descPagerImage) }
        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupArguments() {
        imageRes = arguments?.getInt(KEY_PRESENT_IMAGE_RES)
    }

    private fun setupPagerImage() {
        GlideApp.with(this)
            .load(imageRes)
            .centerCrop()
            .override(displayWidth() / 2)
            .into(binding.descPagerImage)
    }
}
