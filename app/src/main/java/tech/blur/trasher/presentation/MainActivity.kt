package tech.blur.trasher.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.presentation.view.SupportBackStack


class MainActivity : AppCompatActivity() {

    private val accountRepository: AccountRepository by inject()

    val mainActivityViewModel: MainActivityViewModel by inject()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar_mainActivity)

        if (!accountRepository.isUserLoggedIn) {
            navHost_mainActivity.findNavController()
                .navigate(R.id.action_mapFragment_to_loginFragment)
        }

        fab_activityMain.clicks()
            .subscribe {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) run {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA), 2
                    )
                } else {
                    navHost_mainActivity.findNavController()
                        .navigate(R.id.action_mapFragment_to_qrScannerFragment)
                }
            }.addTo(disposable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
                bottomNavDrawerFragment.setDestinationListener {
                    when (it) {
                        R.id.nav_map -> {
                            navHost_mainActivity.findNavController().navigate(R.id.mapFragment)
                        }
                        R.id.nav_profile -> {
//                        navHost_mainActivity.findNavController().navigate(R.id.)

                        }
                        R.id.nav_bonus -> {
//                            navHost_mainActivity.findNavController().navigate(R.id.)

                        }
                        R.id.nav_about -> {
//                        navHost_mainActivity.findNavController().navigate(R.id.)
                        }
                        R.id.nav_eco -> {
//                        navHost_mainActivity.findNavController().navigate(R.id.)
                        }
                        R.id.nav_settings -> {
                            navHost_mainActivity.findNavController()
                                .navigate(R.id.action_mapFragment_to_settingsFragment)
                        }
                    }
                }
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
            p.setMargins(0, 0, 0, 0)
            bottomAppBar_mainActivity.visibility = View.GONE
            fab_activityMain.hide()
        } else {
            p.setMargins(0, 0, 0, 56)
            bottomAppBar_mainActivity.visibility = View.VISIBLE
            fab_activityMain.show()
        }

        frameLayout_mainActivity_navHostContainer.layoutParams = p
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 2)
            navHost_mainActivity.findNavController()
                .navigate(R.id.action_mapFragment_to_qrScannerFragment)
    }

    override fun onBackPressed() {
        if (navHost_mainActivity is SupportBackStack) {
            when {
                (navHost_mainActivity as SupportBackStack).popBackStack() -> return
            }
        }

        moveTaskToBack(true)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}


