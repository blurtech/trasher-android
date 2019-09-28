package tech.blur.trasher.presentation.trashEjection

import android.graphics.Color
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Count
import tech.blur.trasher.domain.EjectTrashRequest
import tech.blur.trasher.domain.TrashcanInfo
import tech.blur.trasher.domain.TrashcanType
import tech.blur.trasher.presentation.BaseViewModel
import tech.blur.trasher.presentation.auth.LoginViewModel
import java.util.*

class TrashEjectionViewModel(
    val userSession: UserSession,
    api: TrasherApi,
    schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val ejectTrash = PublishSubject.create<Int>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableEjectResult = MutableLiveData<Boolean>()
    val ejectResult: LiveData<Boolean> = mutableEjectResult

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage


    var trashType: String = ""

    init {
        ejectTrash
            .withLatestFrom(userSession.qrCodeDataObservable())
            .flatMapSingle {
                val trashcanInfo = Gson().fromJson(it.second, TrashcanInfo::class.java)
                val types = SparseArray<Count>()
                types.put(trashcanInfo.type, Count(-1, it.first))
                Single.just(EjectTrashRequest(trashcanInfo.id, types))
            }
            .flatMapSingle {
                api.ejectTrash(it).toResult()
            }
            .subscribeOn(schedulerProvider.ui())
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

        userSession.qrCodeDataObservable()
            .subscribe {
                val trashcanInfo = Gson().fromJson(it, TrashcanInfo::class.java)
                trashType = TrashcanType.values()[trashcanInfo.type].name.toLowerCase(Locale.getDefault()).capitalize()
            }.addTo(compositeDisposable)
    }
}