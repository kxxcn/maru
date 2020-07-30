package dev.kxxcn.maru

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.kxxcn.maru.util.*
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.home.HomeFragment
import dev.kxxcn.maru.view.more.MoreFragment
import dev.kxxcn.maru.view.tasks.TasksFragment
import kotlinx.android.synthetic.main.maru_activity.*
import kotlinx.coroutines.*
import me.ibrahimsn.lib.OnItemSelectedListener
import org.jetbrains.anko.contentView

@FlowPreview
@ExperimentalCoroutinesApi
class MaruActivity : AppCompatActivity() {

    private val viewModel by viewModels<MaruViewModel>()

    private var navigatorVisible = true

    private var navigatorJob: Job? = null

    private val navOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(host_fragment.findNavController().graph.startDestination, false)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maru_activity)
        setupNavController()
        setupBottomNavigator(savedInstanceState)
        setupSnackbar()
        setupListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_NAVIGATOR_SAVED_STATE_KEY, bottom_navigator.getActiveItem())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_GOOGLE_SIGN_IN -> current()?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        current()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (current() is HomeFragment) {
            viewModel.onBackPressed()
        } else if (current() is TasksFragment || current() is MoreFragment) {
            bottom_navigator.setActiveItem(NAV_HOME)
            navigate(NAV_HOME)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        navigatorJob?.cancel()
        super.onDestroy()
    }

    private fun setupTheme() {
        val themeRes = if (PreferenceUtils.useDarkMode) R.style.AppDarkTheme else R.style.AppTheme
        setTheme(themeRes)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = AttrsUtils.getColor(this, R.attr.statusBarColor)
    }

    private fun setupNavController() {
        host_fragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                getString(R.string.nav_label_splash),
                getString(R.string.nav_label_intro),
                getString(R.string.nav_label_register),
                getString(R.string.nav_label_onboard),
                getString(R.string.nav_label_input),
                getString(R.string.nav_label_status),
                getString(R.string.nav_label_sort),
                getString(R.string.nav_label_order),
                getString(R.string.nav_label_purchase),
                getString(R.string.nav_label_present),
                getString(R.string.nav_label_timeline),
                getString(R.string.nav_label_landmark),
                getString(R.string.nav_label_days),
                getString(R.string.nav_label_days_add),
                getString(R.string.nav_label_notice),
                getString(R.string.nav_label_setting),
                getString(R.string.nav_label_notification),
                getString(R.string.nav_label_edit),
                getString(R.string.nav_label_edit_dialog),
                getString(R.string.nav_label_terms),
                getString(R.string.nav_label_backup) -> false
                else -> true
            }.also {
                navigator_layout.isVisible = it
                openNavigator(it)
            }
        }
    }

    private fun setupBottomNavigator(savedInstanceState: Bundle?) {
        with(bottom_navigator) {
            setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelect(pos: Int) {
                    navigate(pos)
                }
            })
            setActiveItem(savedInstanceState?.getInt(BOTTOM_NAVIGATOR_SAVED_STATE_KEY) ?: 0)
        }
    }

    private fun setupSnackbar() {
        contentView?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.finishEvent.observe(this, EventObserver {
            finish()
        })
    }

    private fun current(): Fragment? {
        val host = host_fragment as? NavHostFragment
        return host?.childFragmentManager?.fragments?.get(0)
    }

    fun navigate(pos: Int) {
        bottom_navigator.setActiveItem(pos)
        with(host_fragment.findNavController()) {
            when (pos) {
                NAV_HOME -> R.id.home_fragment
                NAV_TASKS -> R.id.tasks_fragment
                NAV_SETTINGS -> R.id.more_fragment
                else -> throw RuntimeException("Invalid Position.")
            }.also {
                navigate(it, null, navOptions)
            }
        }
    }

    fun openNavigator(isShowing: Boolean) {
        if (navigatorVisible == isShowing) return
        navigatorVisible = isShowing
        val value = if (navigatorVisible) 0f else navigator_layout.height.toFloat()
        navigatorJob?.cancel()
        navigatorJob = GlobalScope.launch(Dispatchers.Main) {
            delay(NAV_ANIMATE_DELAY)
            ObjectAnimator.ofFloat(
                navigator_layout,
                "translationY",
                value
            ).apply { duration = NAV_ANIMATE_DURATION }.also { it.start() }
        }
    }
}
