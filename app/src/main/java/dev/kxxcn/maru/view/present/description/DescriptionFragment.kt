package dev.kxxcn.maru.view.present.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.DescriptionFragmentBinding
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.present.PresentViewModel

class DescriptionFragment : Fragment() {

    private val viewModel by lazy {
        parentFragment?.let {
            ViewModelProvider(it).get(PresentViewModel::class.java)
        } ?: throw Exception("Invalid Fragment.")
    }

    private lateinit var binding: DescriptionFragmentBinding

    private lateinit var adapter: DescriptionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DescriptionFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@DescriptionFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
        setupPagerAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrollToTop()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.present.observe(viewLifecycleOwner, Observer { present ->
            binding.descPager.currentItem = 0
            present?.let { adapter.notify(it) }
        })
        viewModel.scrollEvent.observe(viewLifecycleOwner, EventObserver {
            binding.descScroll.fullScroll(ScrollView.FOCUS_UP)
        })
    }

    private fun setupPagerAdapter() {
        with(binding.descPager) {
            this@DescriptionFragment.adapter = DescriptionPagerAdapter(childFragmentManager)
            this.adapter = this@DescriptionFragment.adapter
            this.setPadding(0, 0, 30.px, 0)
            this.pageMargin = 10.px
        }

        binding.descIndicator.setViewPager(binding.descPager)
    }
}
