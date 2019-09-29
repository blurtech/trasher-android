package tech.blur.trasher.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.rxkotlin.addTo
import org.koin.android.ext.android.inject
import tech.blur.trasher.R
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.databinding.FragmentTrashcaninfoBinding
import tech.blur.trasher.domain.Trashcan
import tech.blur.trasher.presentation.BaseFragment

class TrashcanInfoSheetFragment : BaseFragment() {

    lateinit var binding: FragmentTrashcaninfoBinding

    private val trashRepository: TrashRepository by inject()
    lateinit var trashcan: Trashcan

    private val schedulerProvider: SchedulerProvider by inject()
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
        userSession.trashcanClickedObservable()
            .observeOn(schedulerProvider.ui())
            .subscribe { id ->
                trashcan = trashRepository.trashCans.list.find { it.id == id }!!
                initSheet(trashcan)
            }.addTo(compositeDisposable)
    }

    private fun initSheet(trashcan: Trashcan) {
        binding.title.text = trashcan.title
        binding.buttonBuildPath.clicks()
            .subscribe {
                userSession.pathClickedClicked(trashcan)
            }.addTo(compositeDisposable)
    }

}