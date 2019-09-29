package tech.blur.trasher.presentation.trashEjection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Count
import tech.blur.trasher.domain.EjectTrashRequest
import tech.blur.trasher.domain.TrashcanType
import tech.blur.trasher.presentation.BaseViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class TrashEjectionViewModel(
    userSession: UserSession,
    api: TrasherApi,
    schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val ejectTrash = PublishSubject.create<Int>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val mutableEjectResult = MutableLiveData<Boolean>()
    val ejectResult: LiveData<Boolean> = mutableEjectResult

    private val mutableCanType = MutableLiveData<String>()
    val canType: LiveData<String> = mutableCanType


    var trashType: String by Delegates.observable("") { _, _, newValue ->
        mutableCanType.value = newValue
    }

    init {
        ejectTrash
            .withLatestFrom(userSession.qrCodeCanDataObservable())
            .flatMapSingle {
                val types = ArrayList<Count>()
                trashType =
                    TrashcanType.values()[it.second.canType].name.toLowerCase(Locale.getDefault())
                        .capitalize()
                types.add(Count(it.second.canType, -1, it.first))
                Single.just(EjectTrashRequest(it.second.id, types))
            }
            .flatMapSingle {
                api.ejectTrash(it).toResult()
            }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableEjectResult.value = true
                    }
                    is Result.Failure -> {
                        mutableEjectResult.value = false
                        mutableErrorMessage.value = "Проблемы с сетью"

                    }
                }
            }.addTo(compositeDisposable)

        userSession.qrCodeCanDataObservable()
            .observeOn(schedulerProvider.ui())
            .subscribe {
                trashType = TrashcanType.values()[it.canType].name.toLowerCase(Locale.getDefault())
                    .capitalize()
                mutableCanType.value = trashType
            }.addTo(compositeDisposable)
    }
}