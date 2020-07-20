package dev.kxxcn.maru.view.more

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.orhanobut.logger.Logger
import dev.kxxcn.maru.BuildConfig
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.MoreFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.REQUEST_CODE_PERMISSION
import dev.kxxcn.maru.util.RESULT_GOOGLE_SIGN_IN
import dev.kxxcn.maru.util.extension.displayHeight
import dev.kxxcn.maru.util.extension.displayWidth
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.util.preference.PreferenceUtils
import org.jetbrains.anko.contentView
import java.io.File
import java.io.FileOutputStream

class MoreFragment : Fragment() {

    private val viewModel by viewModels<MoreViewModel>()

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: MoreFragmentBinding

    private var alertDialog: AlertDialog? = null

    private val client: GoogleSignInClient? by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.maru_web_client_id))
            .requestEmail()
            .build()
        context.takeIf { it != null }?.let {
            GoogleSignIn.getClient(it, gso)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoreFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@MoreFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupArguments()
        setupListAdapter()
        setupSnackbar()
        setupListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_GOOGLE_SIGN_IN -> {
                if (resultCode == RESULT_OK) {
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.getResult(ApiException::class.java)
                        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                        auth.signInWithCredential(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                viewModel.handleSignInSuccess()
                            } else {
                                viewModel.handleSignInFailure()
                            }
                        }
                    } catch (e: Exception) {
                        viewModel.handleSignInFailure()
                    }
                } else {
                    viewModel.handleSignInFailure()
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
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> nightDialog()
        }
    }

    override fun onDestroyView() {
        if (::binding.isInitialized) {
            binding.moreList.adapter = null
        }
        alertDialog = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupArguments() {
        auth = FirebaseAuth.getInstance()
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        with(binding.moreList) {
            addItemDecoration(LinearSpacingDecoration(), 0)
            val activity = requireActivity() as MaruActivity
            adapter = MoreAdapter(viewModel, activity)
        }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.settingEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToSettingFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.contactEvent.observe(viewLifecycleOwner, EventObserver {
            contactDialog()
        })
        viewModel.noticeEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToNoticeFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.storeEvent.observe(viewLifecycleOwner, EventObserver {
            openStore()
        })
        viewModel.orderEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToOrderFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            if (isSignedIn()) {
                openPayment()
            } else {
                signInDialog()
            }
        })
        viewModel.backupEvent.observe(viewLifecycleOwner, EventObserver {
            if (isSignedIn()) {
                openPayment()
            } else {
                signInDialog()
            }
        })
        viewModel.nightEvent.observe(viewLifecycleOwner, EventObserver {
            nightDialog()
        })
        viewModel.daysEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToDayFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.timelineEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToTimelineFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.presentEvent.observe(viewLifecycleOwner, EventObserver { filterType ->
            MoreFragmentDirections.actionMoreFragmentToPresentFragment(filterType).also {
                findNavController().navigate(it)
            }
        })
        viewModel.landmarkEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToLandmarkFragment().also {
                findNavController().navigate(it)
            }
        })
    }

    private fun openStore() {
        val packageName = context?.packageName
        try {
            val uri = getString(R.string.play_store_app, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        } catch (e: ActivityNotFoundException) {
            val uri = getString(R.string.play_store_web, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }

    private fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "plain/Text"
            val emails = arrayOf(getString(R.string.email_address))
            putExtra(Intent.EXTRA_EMAIL, emails)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(
                Intent.EXTRA_TEXT, getString(
                    R.string.email_text,
                    Build.DEVICE,
                    Build.VERSION.SDK_INT,
                    BuildConfig.VERSION_NAME
                )
            )
            type = "message/rfc822"
        })
    }

    private fun signIn() {
        val intent = client?.signInIntent ?: return
        startActivityForResult(intent, RESULT_GOOGLE_SIGN_IN)
    }

    private fun switch() {
        val activity = activity as? MaruActivity ?: return
        val container = activity.contentView

        val cache = context?.cacheDir
        if (cache == null || container == null) {
            activity.recreate()
        } else {
            Bitmap.createBitmap(
                displayWidth(),
                displayHeight(),
                Bitmap.Config.ARGB_8888
            ).also {
                container.draw(Canvas(it))
                val file = File(cache, "capture.png")
                var output: FileOutputStream? = null
                try {
                    file.createNewFile()
                    output = FileOutputStream(file)
                    it.compress(Bitmap.CompressFormat.PNG, 100, output)
                } catch (e: Exception) {
                    Logger.d(e.message)
                } finally {
                    output?.flush()
                    output?.close()
                    PreferenceUtils.useDarkMode = !PreferenceUtils.useDarkMode
                    activity.recreate()
                }
            }
        }
    }

    private fun contactDialog() {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_email,
            getString(R.string.send_email),
            negative = { handleNegativeSelection() },
            positive = { handleContactSelection() }
        )
    }

    private fun signInDialog() {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_sign_in,
            getString(R.string.need_sign_in_process),
            negative = { handleNegativeSelection() },
            positive = { handleSignInSelection() }
        )
    }

    private fun nightDialog() {
        val text = if (PreferenceUtils.useDarkMode) {
            getString(R.string.inactive_night_mode)
        } else {
            getString(R.string.active_night_mode)
        }
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_moon,
            text,
            negative = { handleNegativeSelection() },
            positive = { handleSwitchSelection() }
        )
    }

    private fun handleNegativeSelection() {
        alertDialog?.dismiss()
    }

    private fun handleContactSelection() {
        alertDialog?.dismiss()
        sendEmail()
    }

    private fun handleSignInSelection() {
        alertDialog?.dismiss()
        signIn()
    }

    private fun handleSwitchSelection() {
        alertDialog?.dismiss()
        switch()
    }

    private fun openPayment() {
        MoreFragmentDirections.actionMoreFragmentToPurchaseFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun isSignedIn() = FirebaseAuth.getInstance().currentUser != null
}
