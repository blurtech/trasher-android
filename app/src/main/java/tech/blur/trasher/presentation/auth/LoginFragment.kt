package tech.blur.trasher.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.trasher.R
import tech.blur.trasher.common.ext.observe
import tech.blur.trasher.common.ext.observeNonNull
import tech.blur.trasher.databinding.FragmentLoginBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide

class LoginFragment : BaseFragment(), SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null

    private val loginViewModel: LoginViewModel by viewModel()

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        hideNavigation?.invoke(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideNavigation?.invoke(true)
        binding.editSigninLogin.textChanges()
            .subscribe {
                loginViewModel.login.onNext(it.toString())
            }.addTo(compositeDisposable)

        binding.editSigninPassword.textChanges()
            .subscribe {
                loginViewModel.password.onNext(it.toString())
            }.addTo(compositeDisposable)

        binding.buttonSignin.clicks()
            .subscribe {
                    loginViewModel.loginSubject.onNext(Unit)
            }.addTo(compositeDisposable)

        binding.buttonSignup
            .clicks()
            .subscribe {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }.addTo(compositeDisposable)

        loginViewModel.loginResult.observeNonNull(this) {
            if (it == LoginViewModel.LoginResult.SUCCESS) {
                findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
            }
        }

        loginViewModel.errorMessage.observeNonNull(this) {
            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage(it)
                .setPositiveButton("Ok") { _, _ ->

                }
            dialog.show()
        }

        loginViewModel.networkProgress.observeNonNull(this) {
            if (it) binding.buttonSignin.startAnimation()
            else binding.buttonSignin.revertAnimation()
        }

        loginViewModel.areRequiredFieldsFilled.observeNonNull(this) {
            binding.buttonSignin.isEnabled = it
        }
    }
}
