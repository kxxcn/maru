package dev.kxxcn.maru.view.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.User
import dev.kxxcn.maru.databinding.RegisterFragmentBinding
import dev.kxxcn.maru.util.extension.*
import dev.kxxcn.maru.view.register.RegisterFilterType.*
import org.jetbrains.anko.sdk27.coroutines.onTouch
import java.util.*
import javax.inject.Inject

class RegisterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RegisterViewModel> { viewModelFactory }

    private lateinit var filterType: RegisterFilterType

    private lateinit var binding: RegisterFragmentBinding

    private var datePicker: DatePickerDialog? = null

    private val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private var name = ""

    private var wedding = 0L

    private var editable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@RegisterFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupDatePicker()
        setupFilterType()
        setupMotionLayout()
        setupEditText()
        setupBackPressed()
    }

    override fun onDestroyView() {
        datePicker?.dismiss()
        datePicker = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupDatePicker() {
        val ctx = context ?: return
        datePicker = DatePickerDialog(
            ctx,
            { _, y, m, d ->
                Calendar.getInstance().run {
                    set(y, m, d, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                    timeInMillis.also { wedding = it }
                }.also { viewModel.setInputText(it.msToDate()) }
            }, today.year(), today.month(), today.day()
        ).also { it.datePicker.minDate = today.timeInMillis }
    }

    private fun setupFilterType() {
        REGISTER_NAME.also { filter ->
            filterType = filter
            viewModel.setFiltering(filter)
        }
    }

    private fun setupMotionLayout() {
        binding.registerMotion.apply {
            transitionToEnd()
            setTransitionCompleteListener { _, _ ->
                if (editable) {
                    editable = false
                    setFiltering()
                    transitionToEnd()
                }
            }
        }
        viewModel.motion.observe(viewLifecycleOwner, Observer {
            hideKeyboard()
            if (filterType == REGISTER_BUDGET) {
                val iconIds = resources.getStringArray(R.array.task_icons)
                val budget = binding.infoEdit.text.toString().replace(",", "").toLong()
                viewModel.saveUser(
                    User(
                        name = name,
                        wedding = wedding,
                        budget = budget
                    ),
                    resources.getStringArray(R.array.task_list).mapIndexed { index, name ->
                        Task(
                            name = name,
                            iconId = iconIds[index]
                        )
                    }
                )
                RegisterFragmentDirections.actionRegisterFragmentToHomeFragment().also {
                    findNavController().navigate(it)
                }
                return@Observer
            } else if (filterType == REGISTER_NAME) {
                name = binding.infoEdit.text.toString()
            }
            editable = true
            binding.registerMotion.transitionToStart()
        })
    }

    private fun setupEditText() {
        with(binding.infoEdit) {
            onTouch { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (filterType == REGISTER_WEDDING) {
                        showDatePicker()
                    }
                }
            }
        }
    }

    private fun setFiltering() {
        when (filterType) {
            REGISTER_NAME -> REGISTER_WEDDING
            REGISTER_WEDDING -> REGISTER_BUDGET
            else -> REGISTER_COMPLETE
        }.also {
            filterType = it
            viewModel.setFiltering(it)
        }
        binding.infoEdit.text = null
    }

    private fun setupBackPressed() {
        viewModel.backPressedEvent.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private fun showDatePicker() {
        if (datePicker?.isShowing == true) {
            datePicker?.dismiss()
            return
        }
        datePicker?.show()
    }
}
