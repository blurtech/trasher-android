package tech.blur.trasher.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import tech.blur.trasher.R
import tech.blur.trasher.presentation.auth.LoginFragment
import tech.blur.trasher.presentation.auth.RegistrationFragment
import tech.blur.trasher.presentation.view.SupportBackStack
import tech.blur.trasher.presentation.view.SupportNavigationHide

class TrasherNavHostFragment : NavHostFragment(), SupportBackStack {
    var hideNavigation: ((Boolean) -> Unit)? = null
    var onBackPressed: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.setGraph(R.navigation.graph_app)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is SupportNavigationHide -> fragment.hideNavigation = hideNavigation
            else -> {
                hideNavigation?.invoke(false)
            }
        }
    }

    override fun resetBackStack() {
//        navController.popBackStack(R.id.mapFragment, true)
    }

    override fun popBackStack(): Boolean {
        if (navController.currentDestination!!.id == navController.graph.startDestination) {
            return false
        }

        navController.popBackStack()
        return true
    }

    override fun onBackPressed() {
        onBackPressed?.invoke()
    }
}