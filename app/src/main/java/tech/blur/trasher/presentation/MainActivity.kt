package tech.blur.trasher.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.data.AccountRepository


class MainActivity : AppCompatActivity() {

    private val accountRepository: AccountRepository by inject()

    val mainActivityViewModel: MainActivityViewModel by inject()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar_mainActivity)

        if (!accountRepository.isUserLoggedIn) {
            navHost_mainActivity.findNavController()
                .navigate(R.id.action_mapFragment_to_loginFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is TrasherNavHostFragment -> fragment.hideNavigation = ::hideNavBar
        }
    }

    private fun hideNavBar(hide: Boolean) {
        val p =
            frameLayout_mainActivity_navHostContainer.layoutParams as CoordinatorLayout.LayoutParams

        if (hide) {
            p.setMargins(0, 0, 0, 56)
            bottomAppBar_mainActivity.visibility = View.GONE
            fab_activityMain.hide()
        } else {
            p.setMargins(0, 0, 0, 56)
            bottomAppBar_mainActivity.visibility = View.VISIBLE
            fab_activityMain.show()
        }

        frameLayout_mainActivity_navHostContainer.layoutParams = p


    }

    override fun onBackPressed() {
        navHost_mainActivity.findNavController().popBackStack()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHost_mainActivity)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}


