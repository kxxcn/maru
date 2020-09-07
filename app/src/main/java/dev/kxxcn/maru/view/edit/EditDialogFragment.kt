package dev.kxxcn.maru.view.edit

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import dev.kxxcn.maru.databinding.EditDialogFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.extension.displayHeight
import dev.kxxcn.maru.view.base.BaseDialogFragment
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import javax.inject.Inject

class EditDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: EditDialogFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<EditDialogViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

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
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
