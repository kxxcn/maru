package dev.kxxcn.maru.view.edit

import android.app.Dialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.EditDialogFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.extension.displayHeight
import dev.kxxcn.maru.view.base.BaseDialogFragment
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import java.util.Calendar
import javax.inject.Inject

class EditDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var adHelper: AdHelper

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: EditDialogFragmentBinding

    private var weddingDatePicker: DatePickerDialog? = null

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<EditDialogViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.let {
                it.requestFeature(Window.FEATURE_NO_TITLE)
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(matchParent, wrapContent)
            it.attributes = it.attributes.apply {
                height = (displayHeight() * 0.9).toInt()
                gravity = Gravity.CENTER
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditDialogFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@EditDialogFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupWeddingDatePicker()
        setupListener()
        setupInterstitial()
    }

    override fun onDestroyView() {
        weddingDatePicker?.dismiss()
        weddingDatePicker = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupWeddingDatePicker() {
        if (!viewModel.isWedding) return

        viewModel.weddingDate.observe(viewLifecycleOwner) { wedding ->
            if (weddingDatePicker != null) return@observe

            val selected = Calendar.getInstance().apply {
                timeInMillis = wedding
            }
            weddingDatePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    Calendar.getInstance().apply {
                        set(year, month, day, 0, 0, 0)
                        set(Calendar.MILLISECOND, 0)
                        viewModel.setWeddingDate(timeInMillis)
                    }
                },
                selected.get(Calendar.YEAR),
                selected.get(Calendar.MONTH),
                selected.get(Calendar.DAY_OF_MONTH)
            )
            binding.editInput.setOnClickListener { showWeddingDatePicker() }
        }
    }

    private fun showWeddingDatePicker() {
        if (weddingDatePicker?.isShowing == true) {
            weddingDatePicker?.dismiss()
        } else {
            weddingDatePicker?.show()
        }
    }

    private fun setupListener() {
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            requestAd()
        })
    }

    private fun setupInterstitial() {
        val id = getString(R.string.admob_interstitial_edit_id)
        adHelper.loadInterstitialAd(
            id = id,
            onLoaded = {
                if (adHelper.isRequested) {
                    adHelper.show(requireActivity())
                }
            },
            onDismissed = {
                viewModel.close()
            },
            onFailedToShow = {
                viewModel.close()
            }
        )
    }

    private fun requestAd() {
        with(adHelper) {
            request()
            if (isLoaded) {
                show(requireActivity())
            } else {
                viewModel.close()
            }
        }
    }
}
