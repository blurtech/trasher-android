package tech.blur.trasher.presentation.profile

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.trasher.R
import tech.blur.trasher.common.ext.observeNonNull
import tech.blur.trasher.databinding.FragmentProfileBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide


class UserProfileFragment : BaseFragment(), SupportNavigationHide {

    override var hideNavigation: ((Boolean) -> Unit)? = null

    lateinit var binding: FragmentProfileBinding

    private val userProfileViewModel: UserProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        userProfileViewModel.loadProfileSubject.onNext(Unit)

        userProfileViewModel.loadResult.observeNonNull(this) {
            binding.textViewProfileName.text = it.username
            binding.textViewProfileParcels.text = it.bags.toString()
            binding.textViewProfilePoints.text = it.points.toString()
            binding.editViewProfileCity.text = it.address.city
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideNavigation?.invoke(true)
        userProfileViewModel.errorMessage.observeNonNull(this) {
            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage(it)
                .setPositiveButton("Потворить") { _, _ ->
                    userProfileViewModel.retry()
                }
            dialog.show()
        }

        userProfileViewModel.networkProgress.observeNonNull(this) {
            binding.progressBarProfile.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.buttonProfileStore.clicks()
            .subscribe {
                findNavController().navigate(R.id.action_userProfileFragment_to_storeFragment)
            }.addTo(compositeDisposable)

        binding.buttonEdit.clicks()
            .subscribe {
                val dialog = AlertDialog.Builder(context!!)
                dialog.setMessage("Увы, пока что нельзя редактировать свой профиль")
                    .setPositiveButton("Ок") { _, _ ->
                        userProfileViewModel.retry()
                    }
                dialog.show()
            }.addTo(compositeDisposable)

        binding.imageViewProfileGame.setImageDrawable(buildGameImage())
    }

    private fun buildGameImage(): LayerDrawable {
        val layer1 = context?.resources?.getDrawable(R.drawable.ic_food1, null)!!
        val layer2 = context?.resources?.getDrawable(R.drawable.ic_dangerous1, null)!!
        val layer3 = context?.resources?.getDrawable(R.drawable.ic_paper1, null)!!
        val layer4 = context?.resources?.getDrawable(R.drawable.ic_plast1, null)!!
        val layer5 = context?.resources?.getDrawable(R.drawable.ic_glass2, null)!!
        val layers = arrayOf(layer1, layer2, layer3, layer4, layer5)
        return LayerDrawable(layers)
    }

}