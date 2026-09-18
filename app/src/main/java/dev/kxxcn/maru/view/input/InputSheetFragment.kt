package dev.kxxcn.maru.view.input

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.InputSheetFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.view.base.BaseDialogFragment
import javax.inject.Inject

/**
 * 지출 입력 하단 시트. 신랑, 신부, 잔금 필드를 골라 키패드와 단위 칩으로 입력한다.
 */
class InputSheetFragment : BaseDialogFragment() {

    @Inject
    lateinit var adHelper: AdHelper

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: InputSheetFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<InputViewModel> {
        viewModelFactory.create(this, arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Maru_Sheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InputSheetFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = this@InputSheetFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setupInsets()
        setupListener()
        setupInterstitial()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.5f)
        }
    }

    private fun setupInsets() {
        val basePadding = binding.inputSheet.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.inputSheet) { v, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            v.updatePadding(bottom = basePadding + bottom)
            insets
        }
    }

    private fun setupListener() {
        viewModel.doneEvent.observe(viewLifecycleOwner, EventObserver {
            openStatusFragment()
        })
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            showAd()
        })
    }

    private fun setupInterstitial() {
        val id = getString(R.string.admob_interstitial_task_id)
        adHelper.loadInterstitialAd(
            id = id,
            onLoaded = {
                if (adHelper.isRequested) {
                    adHelper.show(requireActivity())
                }
            },
            onShowed = {
                openStatusFragment()
            },
            onFailedToShow = {
                openStatusFragment()
            }
        )
    }

    private fun showAd() {
        try {
            with(adHelper) {
                request()
                if (isLoaded) {
                    show(requireActivity())
                } else {
                    openStatusFragment()
                }
            }
        } catch (e: Exception) {
            openStatusFragment()
        }
    }

    private fun openStatusFragment() {
        if (!isAdded) return
        findNavController().navigate(R.id.action_inputFragment_to_statusFragment)
    }
}
