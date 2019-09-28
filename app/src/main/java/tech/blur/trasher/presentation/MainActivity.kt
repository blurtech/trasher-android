package tech.blur.trasher.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.presentation.auth.LoginFragment
import tech.blur.trasher.presentation.auth.RegistrationFragment

class MainActivity : AppCompatActivity() {

    private val accountRepository: AccountRepository by inject()

    val mainActivityViewModel: MainActivityViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!accountRepository.isUserLoggedIn) {
            container_main.findNavController().navigate(R.id.action_mapFragment_to_loginFragment)
        }

    }

    override fun onAttachFragment(fragment: Fragment) {
            when(fragment){
                is TrasherNavHostFragment -> fragment.hideNavigation = ::hideNavBar
        }
    }

    private fun hideNavBar(hide: Boolean) {
        bottomAppBar_mainActivity.visibility = if (hide) View.GONE else View.VISIBLE
    }

    override fun onBackPressed() {
        container_main.findNavController().popBackStack()
    }
}


