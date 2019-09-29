package tech.blur.trasher.presentation.profile

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
import tech.blur.trasher.domain.Statistic
import tech.blur.trasher.domain.TrashcanType
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
        userProfileViewModel.loadProfileRatingSubject.onNext(Unit)

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

        userProfileViewModel.statResult.observeNonNull(this) {
            binding.imageViewProfileGame.setImageDrawable(buildGameImage(it))
        }

        binding.buttonEdit.clicks()
            .subscribe {
                val dialog = AlertDialog.Builder(context!!)
                dialog.setMessage("Увы, пока что нельзя редактировать свой профиль")
                    .setPositiveButton("Ок") { _, _ ->
                        userProfileViewModel.retry()
                    }
                dialog.show()
            }.addTo(compositeDisposable)
    }

    private fun buildGameImage(it: ArrayList<Statistic>): LayerDrawable {

        if (it.isEmpty()) {
            val layer = context?.resources?.getDrawable(R.drawable.ic_transparent, null)
            val layers = arrayOf(layer)
            return LayerDrawable(layers)
        }

        var morphToType: ((Int) -> TrashcanType) = {
            TrashcanType.values()[it]
        }

        val layer1Res: Int =
            when (it.find { morphToType(it.type - 1).isOther() }?.value?.total) {
                in 1..50 -> R.drawable.ic_food1
                in 50..100 -> R.drawable.ic_food2
                null -> R.drawable.ic_transparent
                else -> R.drawable.ic_food3
            }
        val layer2Res: Int =
            when (it.find { it.type - 1 == TrashcanType.DANGER.ordinal }?.value?.total) {
                in 1..50 -> R.drawable.ic_dangerous1
                in 50..100 -> R.drawable.ic_dangerous2
                null -> R.drawable.ic_transparent
                else -> R.drawable.ic_dangerous3
            }
        val layer3Res: Int =
            when (it.find { it.type - 1 == TrashcanType.PAPER.ordinal }?.value?.total) {
                in 1..50 -> R.drawable.ic_paper1
                in 50..100 -> R.drawable.ic_paper2
                null -> R.drawable.ic_transparent
                else -> R.drawable.ic_paper3
            }
        val layer4Res: Int =
            when (it.find { it.type - 1 == TrashcanType.PLASTIC.ordinal }?.value?.total) {
                in 1..50 -> R.drawable.ic_plast1
                in 50..100 -> R.drawable.ic_plast2
                null -> R.drawable.ic_transparent
                else -> R.drawable.ic_plast3
            }
        val layer5Res: Int =
            when (it.find { it.type - 1 == TrashcanType.GLASS.ordinal }?.value?.total) {
                in 1..50 -> R.drawable.ic_transparent
                in 50..100 -> R.drawable.ic_glass2
                null -> R.drawable.ic_transparent
                else -> R.drawable.ic_glass3
            }

        val layer1 = context?.resources?.getDrawable(layer1Res, null)!!
        val layer2 = context?.resources?.getDrawable(layer2Res, null)!!
        val layer3 = context?.resources?.getDrawable(layer3Res, null)!!
        val layer4 = context?.resources?.getDrawable(layer4Res, null)!!
        val layer5 = context?.resources?.getDrawable(layer5Res, null)!!

        val layers = arrayOf(layer1, layer2, layer3, layer4, layer5)
        return LayerDrawable(layers)
    }

}