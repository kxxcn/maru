package dev.kxxcn.maru.view.landmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.maru.databinding.LandmarkPagerFragmentBinding
import dev.kxxcn.maru.util.KEY_LANDMARK_FILTER_TYPE
import dev.kxxcn.maru.util.KEY_LANDMARK_LATITUDE
import dev.kxxcn.maru.util.KEY_LANDMARK_LONGITUDE

class LandmarkPagerFragment : Fragment() {

    private val viewModel by lazy {
        parentFragment?.let {
            ViewModelProvider(it).get(LandmarkViewModel::class.java)
        } ?: throw Exception("Invalid Fragment.")
    }

    private lateinit var binding: LandmarkPagerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LandmarkPagerFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@LandmarkPagerFragment.viewModel
            item = arguments?.get(KEY_LANDMARK_FILTER_TYPE) as LandmarkFilterType
            longitude = arguments?.getDouble(KEY_LANDMARK_LONGITUDE)
            latitude = arguments?.getDouble(KEY_LANDMARK_LATITUDE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
