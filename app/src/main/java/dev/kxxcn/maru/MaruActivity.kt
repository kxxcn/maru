package dev.kxxcn.maru

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.kxxcn.maru.databinding.MaruActivityBinding
import dev.kxxcn.maru.util.*
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.Scrollable
import dev.kxxcn.maru.view.base.Signinable
import dev.kxxcn.maru.view.home.HomeFragment
import dev.kxxcn.maru.view.more.MoreFragment
import dev.kxxcn.maru.view.tasks.TasksFragment
import kotlinx.coroutines.*
import me.ibrahimsn.lib.OnItemReselectedListener
import me.ibrahimsn.lib.OnItemSelectedListener

@FlowPreview
@ExperimentalCoroutinesApi
class MaruActivity : AppCompatActivity() {

    private lateinit var binding: MaruActivityBinding

    private val viewModel by viewModels<MaruViewModel>()

    private var navigatorVisible = true

    private var navigatorJob: Job? = null

    private var systemBarBottom = 0

    private var navigatorLayoutHeight = 0

    private val navHostFragment: NavHostFragment
        get() = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment

    private val navOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navHostFragment.findNavController().graph.startDestinationId, false)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        binding = MaruActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupNavController()
        setupBottomNavigator(savedInstanceState)
        setupSnackbar()
        setupListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_NAVIGATOR_SAVED_STATE_KEY, binding.bottomNavigator.getActiveItem())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_GOOGLE_SIGN_IN -> {
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        (current() as? Signinable)?.onSuccess(data)
                    } else {
                        throw RuntimeException("Invalid Account.")
                    }
                } catch (e: Exception) {
                    (current() as? Signinable)?.onFailure()
                }
            }
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
            binding.bottomNavigator.setActiveItem(NAV_HOME)
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

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !PreferenceUtils.useDarkMode
            isAppearanceLightNavigationBars = !PreferenceUtils.useDarkMode
        }
    }

    private fun setupWindowInsets() {
        navigatorLayoutHeight = binding.navigatorLayout.layoutParams.height
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            systemBarBottom = systemBars.bottom
            binding.navigatorLayout.updatePadding(
                left = systemBars.left,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            binding.navigatorLayout.updateLayoutParams {
                height = navigatorLayoutHeight + systemBars.bottom
            }
            updateNavHostBottomPadding()
            insets
        }
        ViewCompat.requestApplyInsets(binding.root)
    }

    private fun setupNavController() {
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
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
                binding.navigatorLayout.isVisible = it
                openNavigator(it)
                updateNavHostBottomPadding()
            }
        }
    }

    private fun setupBottomNavigator(savedInstanceState: Bundle?) {
        with(binding.bottomNavigator) {
            setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelect(pos: Int) {
                    navigate(pos)
                }
            })
            setOnItemReselectedListener(object : OnItemReselectedListener {
                override fun onItemReselect(pos: Int) {
                    (current() as? Scrollable)?.scrollToTop()
                }
            })
            setActiveItem(savedInstanceState?.getInt(BOTTOM_NAVIGATOR_SAVED_STATE_KEY) ?: 0)
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.snackbarRes, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.finishEvent.observe(this, EventObserver {
            finish()
        })
    }

    private fun current(): Fragment? {
        return navHostFragment.childFragmentManager.fragments.getOrNull(0)
    }

    fun navigate(pos: Int) {
        binding.bottomNavigator.setActiveItem(pos)
        with(navHostFragment.findNavController()) {
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
        updateNavHostBottomPadding()
        val value = if (navigatorVisible) 0f else binding.navigatorLayout.height.toFloat()
        navigatorJob?.cancel()
        navigatorJob = GlobalScope.launch(Dispatchers.Main) {
            delay(NAV_ANIMATE_DELAY)
            ObjectAnimator.ofFloat(
                binding.navigatorLayout,
                "translationY",
                value
            ).apply { duration = NAV_ANIMATE_DURATION }.also { it.start() }
        }
    }

    private fun updateNavHostBottomPadding() {
        val bottomPadding = if (binding.navigatorLayout.isVisible && navigatorVisible) {
            binding.navigatorLayout.layoutParams.height
        } else {
            systemBarBottom
        }
        navHostFragment.view?.updatePadding(bottom = bottomPadding)
    }
}
