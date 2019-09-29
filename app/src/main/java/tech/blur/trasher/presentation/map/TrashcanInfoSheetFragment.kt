package tech.blur.trasher.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.UserSession
import tech.blur.trasher.databinding.FragmentTrashcaninfoBinding
import tech.blur.trasher.presentation.BaseFragment

class TrashcanInfoSheetFragment: BaseFragment() {

    lateinit var binding: FragmentTrashcaninfoBinding

    private val userSession: UserSession by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trashcaninfo,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



    }

}