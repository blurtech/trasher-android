package tech.blur.trasher.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Trashcan
import tech.blur.trasher.domain.Trashlist
import tech.blur.trasher.presentation.BaseViewModel

class MapViewModel(
    api: TrasherApi,
    schedulerProvider: SchedulerProvider,
    trashRepository: TrashRepository
) : BaseViewModel() {

    val loadTrashcans = PublishSubject.create<Unit>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableTrashcans = MutableLiveData<List<Trashcan>>()
    val trashcans: LiveData<List<Trashcan>> = mutableTrashcans

    init {
        loadTrashcans
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle { api.getTrashCans().toResult() }
            .flatMapSingle {
                if (it is Result.Success) {
                    trashRepository.trashCans = Trashlist(it.data.data)
                }
                Single.just(it)
            }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableTrashcans.value = it.data.data
                    }
                    is Result.Failure -> {
                        when (it.throwable) {
                            is HttpException -> {
//                                if (it.throwable.code() == 401)
//                                    //unauth
//                                else
//
                            }
                            else -> {
                                println(it.throwable.message)
                            }
                        }
                    }
                }

                mutableNetworkProgress.value = false
            }.addTo(compositeDisposable)

    }


}
