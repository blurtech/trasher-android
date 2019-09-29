package tech.blur.trasher.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import tech.blur.trasher.R
import tech.blur.trasher.databinding.FragmentStoreBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide

class StoreFragment : BaseFragment(), SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null

    lateinit var binding: FragmentStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideNavigation?.invoke(true)
    }
}