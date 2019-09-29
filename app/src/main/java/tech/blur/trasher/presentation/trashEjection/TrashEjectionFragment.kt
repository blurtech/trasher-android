package tech.blur.trasher.presentation.trashEjection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_trashejection.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.trasher.R
import tech.blur.trasher.common.ext.observe
import tech.blur.trasher.common.ext.observeNonNull
import tech.blur.trasher.databinding.FragmentTrashejectionBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide

class TrashEjectionFragment : BaseFragment(), SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null
    lateinit var binding: FragmentTrashejectionBinding

    private val trashEjectionViewModel: TrashEjectionViewModel by viewModel()

    var bags = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trashejection,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressButtonEjectTrash.clicks()
            .subscribe {
                if (bags != 0) trashEjectionViewModel.ejectTrash.onNext(bags)
            }.addTo(compositeDisposable)

        binding.editTextEjectTrash.textChanges()
            .subscribe {
                if (!it.isNullOrBlank())
                    bags = Integer.parseInt(it.toString())
            }.addTo(compositeDisposable)

        trashEjectionViewModel.networkProgress.observeNonNull(this) {
            if (it) progressButton_ejectTrash.startMorphAnimation()
            else progressButton_ejectTrash.revertAnimation()
        }

        trashEjectionViewModel.ejectResult.observeNonNull(this) {
            if (it) findNavController().popBackStack(R.id.mapFragment, false)
        }

        trashEjectionViewModel.canType.observeNonNull(this) {
            binding.textViewEjectTrashCanType.text = it
        }

        trashEjectionViewModel.errorMessage.observeNonNull(this) {
            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage(it)
                .setPositiveButton("Ok") { _, _ ->

                }
            dialog.show()
        }
    }
}