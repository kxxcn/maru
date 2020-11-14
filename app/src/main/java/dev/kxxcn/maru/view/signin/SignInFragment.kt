package dev.kxxcn.maru.view.signin

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.kxxcn.maru.util.RESULT_GOOGLE_SIGN_IN
import dev.kxxcn.maru.view.base.BaseFragment
import dev.kxxcn.maru.view.base.Signinable
import javax.inject.Inject

abstract class SignInFragment : BaseFragment(), Signinable {

    abstract fun handleSignInSuccess()

    abstract fun handleSignInFailure()

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var client: GoogleSignInClient

    override fun onSuccess(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                handleSignInSuccess()
            } else {
                handleSignInFailure()
            }
        }
    }

    override fun onFailure() {
        handleSignInFailure()
    }

    protected fun signIn() {
        activity?.startActivityForResult(client.signInIntent, RESULT_GOOGLE_SIGN_IN)
    }
}
