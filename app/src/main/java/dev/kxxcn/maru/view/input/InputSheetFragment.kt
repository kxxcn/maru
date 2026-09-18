package dev.kxxcn.maru.view.input

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
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
import java.util.Calendar

/**
 * 지출 입력 하단 시트. 신랑, 신부, 잔금 금액과 지출일·메모를 입력한다.
 */
class InputSheetFragment : BaseDialogFragment() {

    @Inject
    lateinit var adHelper: AdHelper

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: InputSheetFragmentBinding

    private var datePicker: DatePickerDialog? = null

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
        setupInputFields()
        setupListener()
        setupInterstitial()
    }

    override fun onDestroyView() {
        datePicker?.dismiss()
        datePicker = null
        super.onDestroyView()
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
        binding.inputDateField.setOnClickListener { showDatePicker() }
        viewModel.doneEvent.observe(viewLifecycleOwner, EventObserver {
            openStatusFragment()
        })
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            showAd()
        })
    }

    private fun setupInputFields() {
        bindAmountInput(binding.inputHusbandMoney, InputMoneyType.HUSBAND)
        bindAmountInput(binding.inputWifeMoney, InputMoneyType.WIFE)
        bindAmountInput(binding.inputRemainMoney, InputMoneyType.REMAIN)
        binding.inputMemo.doAfterTextChanged { text ->
            viewModel.setMemo(text?.toString().orEmpty())
        }
    }

    private fun bindAmountInput(
        input: android.widget.EditText,
        type: InputMoneyType
    ) {
        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) viewModel.selectField(type)
        }
        input.doAfterTextChanged { text ->
            viewModel.setAmount(type, text?.toString().orEmpty())
        }
    }

    private fun showDatePicker() {
        val selected = Calendar.getInstance().apply {
            timeInMillis = viewModel.selectedDate.value ?: System.currentTimeMillis()
        }
        datePicker?.dismiss()
        datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.also { viewModel.selectDate(it.timeInMillis) }
            },
            selected.get(Calendar.YEAR),
            selected.get(Calendar.MONTH),
            selected.get(Calendar.DAY_OF_MONTH)
        ).also { it.show() }
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
