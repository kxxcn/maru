package dev.kxxcn.maru.view.edit

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerDialogFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.EditDialogFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.extension.displayHeight
import dev.kxxcn.maru.util.extension.setupSnackbar
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import javax.inject.Inject

class EditDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private val viewModel by viewModels<EditDialogViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private lateinit var binding: EditDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let { window ->
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(matchParent, wrapContent)
            window.attributes = window.attributes.apply {
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
        setupSnackbar()
        setupListener()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }
}
