package dev.kxxcn.maru.view.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.SettingFragmentBinding
import dev.kxxcn.maru.util.NotificationUtils
import kotlinx.android.synthetic.main.setting_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SettingFragment : Fragment() {

    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            close = { findNavController().popBackStack() }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        setting_profile_edit.onClick { clickItem(it) }
        setting_tasks_edit.onClick { clickItem(it) }
        setting_notification_notice.onClick { clickItem(it) }
        setting_terms_location_based_service.onClick { clickItem(it) }
        setting_terms_license.onClick { clickItem(it) }
    }

    private fun clickItem(v: View?) {
        v ?: return
        when (v.id) {
            R.id.setting_profile_edit -> profile()
            R.id.setting_tasks_edit -> tasks()
            R.id.setting_notification_notice -> notification()
            R.id.setting_terms_location_based_service -> locationBasedService()
            R.id.setting_terms_license -> license()
        }
    }

    private fun profile() {
        SettingFragmentDirections.actionSettingFragmentToEditFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun tasks() {
        SettingFragmentDirections.actionSettingFragmentToSortFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_CHANNEL_ID, NotificationUtils.CHANNEL_NOTICE)
                putExtra(Settings.EXTRA_APP_PACKAGE, activity?.packageName)
            }.also {
                startActivity(it)
            }
        } else {
            SettingFragmentDirections.actionSettingFragmentToNotificationFragment().also {
                findNavController().navigate(it)
            }
        }
    }

    private fun locationBasedService() {
        SettingFragmentDirections.actionSettingFragmentToTermsFragment(
            R.raw.location_based_service,
            R.string.setting_terms_location_based_service
        ).also { findNavController().navigate(it) }
    }

    private fun license() {
        SettingFragmentDirections.actionSettingFragmentToTermsFragment(
            R.raw.license,
            R.string.setting_terms_license
        ).also { findNavController().navigate(it) }
    }
}
