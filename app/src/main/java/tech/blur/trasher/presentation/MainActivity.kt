package tech.blur.trasher.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.data.AccountRepository

class MainActivity : AppCompatActivity() {

    val accountRepository: AccountRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!accountRepository.isUserLoggedIn) {
            container_main.findNavController().navigate(R.id.action_mapFragment_to_loginFragment)
        }

    }

    override fun onBackPressed() {
        container_main.findNavController().popBackStack()
    }
}


