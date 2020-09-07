package dev.kxxcn.maru.view.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.maru.databinding.NoticeFragmentBinding
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class NoticeFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: NoticeFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<NoticeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoticeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@NoticeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListAdapter()
    }

    override fun onDestroyView() {
        binding.noticeList.adapter = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        binding.noticeList.adapter = NoticeAdapter()
    }
}
