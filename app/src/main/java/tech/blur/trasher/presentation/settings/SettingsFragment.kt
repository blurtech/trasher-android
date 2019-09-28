package tech.blur.trasher.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.rxkotlin.addTo
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.databinding.FragmentSettingsBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide

class SettingsFragment : BaseFragment(), SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null

    private val accountRepository: AccountRepository by inject()
    private val trashRepository: TrashRepository by inject()

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideNavigation?.invoke(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSettingsSignout.clicks()
            .subscribe{
                accountRepository.logOut()
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            }.addTo(compositeDisposable)

        binding.buttonSettingsCleanCachedTrashcans.clicks()
            .subscribe{
                trashRepository.clean()
                Toast.makeText(activity, "Кэш отчишен", Toast.LENGTH_SHORT).show()
            }.addTo(compositeDisposable)
    }
}