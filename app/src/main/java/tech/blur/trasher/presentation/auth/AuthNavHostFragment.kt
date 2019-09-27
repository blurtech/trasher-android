package tech.blur.trasher.presentation.auth

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import tech.blur.trasher.R
import tech.blur.trasher.presentation.view.SupportBackStack

class AuthNavHostFragment: NavHostFragment(), SupportBackStack {
    var onBackPressed: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.setGraph(R.navigation.graph_app)
    }

    override fun resetBackStack() {
//        navController.popBackStack(R.id., false)
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