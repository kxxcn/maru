package dev.kxxcn.maru.view.intro

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.extension.setTransitionCompleteListener
import kotlinx.android.synthetic.main.intro_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class IntroFragment : Fragment(R.layout.intro_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMotionLayout()
        setupTextView()
        setupButtonListener()
    }

    private fun setupMotionLayout() {
        intro_motion.apply {
            setTransitionCompleteListener { _, _ ->
                IntroFragmentDirections.actionIntroFragmentToRegisterFragment().also {
                    findNavController().navigate(it)
                }
            }
        }
    }

    private fun setupTextView() {
        welcome_text.text = SpannableStringBuilder(welcome_text.text).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun setupButtonListener() {
        start_text.onClick { showRegisterScreen() }
    }

    private fun showRegisterScreen() {
        intro_motion.transitionToEnd()
    }
}