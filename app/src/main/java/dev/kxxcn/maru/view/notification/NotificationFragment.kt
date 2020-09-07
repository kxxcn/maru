package dev.kxxcn.maru.view.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dev.kxxcn.maru.databinding.NotificationFragmentBinding
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragment

class NotificationFragment : BaseFragment() {

    private lateinit var binding: NotificationFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@NotificationFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.noticeUse.observe(viewLifecycleOwner, Observer {
            PreferenceUtils.notifyNotice = it
        })
        viewModel.noticeVibrate.observe(viewLifecycleOwner, Observer {
            PreferenceUtils.notifyNoticeVibrate = it
        })
        viewModel.noticeSound.observe(viewLifecycleOwner, Observer {
            PreferenceUtils.notifyNoticeSound = it
        })
    }
}
