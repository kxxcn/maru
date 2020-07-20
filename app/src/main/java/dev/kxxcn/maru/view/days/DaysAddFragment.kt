package dev.kxxcn.maru.view.days

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.DaysAddFragmentBinding
import dev.kxxcn.maru.util.extension.*
import java.util.*
import javax.inject.Inject

class DaysAddFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DaysAddViewModel> { viewModelFactory }

    private lateinit var binding: DaysAddFragmentBinding

    private var datePicker: DatePickerDialog? = null

    private var time = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DaysAddFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@DaysAddFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupSnackbar()
        setupListener()
        setupDatePicker()
    }

    override fun onDestroyView() {
        datePicker?.dismiss()
        datePicker = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            findNavController().popBackStack()
        })
        viewModel.datePickerEvent.observe(viewLifecycleOwner, EventObserver {
            showDatePicker()
        })
        viewModel.selectCount.observe(viewLifecycleOwner, Observer {
            viewModel.setDay(time.timeInMillis)
        })
    }

    private fun setupDatePicker() {
        val ctx = context ?: return
        datePicker = DatePickerDialog(
            ctx,
            DatePickerDialog.OnDateSetListener { _, y, m, d ->
                Calendar.getInstance().apply {
                    set(y, m, d, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.also {
                    time = it
                    viewModel.setTime(it.timeInMillis)
                }
            }, time.year(), time.month(), time.day()
        )
    }

    private fun showDatePicker() {
        if (datePicker?.isShowing == true) {
            datePicker?.dismiss()
            return
        }
        datePicker?.show()
    }
}
