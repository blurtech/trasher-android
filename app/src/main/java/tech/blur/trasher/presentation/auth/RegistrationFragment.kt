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
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.trasher.R
import tech.blur.trasher.common.ext.observeNonNull
import tech.blur.trasher.databinding.FragmentRegistrationBinding
import tech.blur.trasher.presentation.BaseFragment

class RegistrationFragment : BaseFragment() {
    var hideNavigation: ((Boolean) -> Unit)? = null

    private val registrationViewModel: RegistrationViewModel by viewModel()

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration,
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

        binding.editSignupLogin.textChanges()
            .subscribe {
                registrationViewModel.login.onNext(it.toString())
            }.addTo(compositeDisposable)

        binding.editSignupPassword.textChanges()
            .subscribe {
                registrationViewModel.password.onNext(it.toString())
            }.addTo(compositeDisposable)

        binding.registerButton.clicks()
            .subscribe {
                registrationViewModel.loginSubject.onNext(Unit)
            }.addTo(compositeDisposable)

        binding.editSignupCity
            .textChanges()
            .subscribe {
                registrationViewModel.city.onNext(it.toString())
            }.addTo(compositeDisposable)

        registrationViewModel.registrationResult.observeNonNull(this) {
            findNavController().navigate(R.id.action_registrationFragment_to_mapFragment)
        }

        registrationViewModel.errorMessage.observeNonNull(this) {
            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage(it)
                .setPositiveButton("Ok") { _, _ -> }
            dialog.show()
        }

        registrationViewModel.networkProgress.observeNonNull(this) {
            if (it) binding.registerButton.startAnimation()
            else binding.registerButton.revertAnimation()
        }

        registrationViewModel.areRequiredFieldsFilled.observeNonNull(this) {
            binding.registerButton.isEnabled = it
        }
    }
}