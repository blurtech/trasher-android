package tech.blur.trasher.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_navigationview.*
import tech.blur.trasher.R

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    var onItemCliked: ((Int)->Unit)? = null

    var currentItem: Int = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        currentItem = R.id.nav_map

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_map -> {
                    if (currentItem != R.id.nav_map) {
                        onItemCliked?.invoke(R.id.nav_settings)
                        currentItem = R.id.nav_map
                    }
                }
                R.id.nav_profile -> {
                    if (currentItem != R.id.nav_profile) {
//                        findNavController().navigate(R.id.)
//                        currentItem = R.id.nav_profile
                    }
                }
                R.id.nav_about -> {
                    if (currentItem != R.id.nav_about) {
//                        findNavController().navigate(R.id.)
//                        currentItem = R.id.nav_about
                    }
                }
                R.id.nav_eco -> {
                    if (currentItem != R.id.nav_eco) {
//                        findNavController().navigate(R.id.)
//                        currentItem = R.id.nav_eco
                    }
                }
                R.id.nav_settings -> {
                    if (currentItem != R.id.nav_settings) {
                        onItemCliked?.invoke(R.id.nav_settings)
                        currentItem = R.id.nav_settings
                    }
                }
            }
            //TODO перенести код сюда
            dismiss()
            true
        }
    }

    fun setDestinationListener(listener:((Int)->Unit)){
        onItemCliked = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigationview, container, false)
    }
}