package tech.blur.trasher.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Statistic
import tech.blur.trasher.domain.User
import tech.blur.trasher.presentation.BaseViewModel

class UserProfileViewModel(
    userSession: UserSession,
    api: TrasherApi,
    schedulerProvider: SchedulerProvider
): BaseViewModel() {

    val loadProfileSubject = PublishSubject.create<Unit>()

    val loadProfileRatingSubject = PublishSubject.create<Unit>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableLoadResult = MutableLiveData<User>()
    val loadResult: LiveData<User> = mutableLoadResult

    private val mutableStatResult = MutableLiveData<ArrayList<Statistic>>()
    val statResult: LiveData<ArrayList<Statistic>> = mutableStatResult

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    init {
        loadProfileSubject
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle { api.getProfile().toResult() }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableLoadResult.value = it.data.data
                    }
                    is Result.Failure -> {

                        when (it.throwable) {
                            is HttpException -> {
                                    mutableErrorMessage.value = "Какие то проблемы с сетью("
                            }
                            else -> {
                                mutableErrorMessage.value = it.throwable.message
                            }
                        }
                    }
                }
                mutableNetworkProgress.value = false
            }.addTo(compositeDisposable)

        loadProfileRatingSubject
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle { api.getStatistic().toResult() }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableStatResult.value = it.data.data
                    }
                    is Result.Failure -> {

                        when (it.throwable) {
                            is HttpException -> {
                                mutableErrorMessage.value = "Какие то проблемы с сетью("
                            }
                            else -> {
                                mutableErrorMessage.value = it.throwable.message
                            }
                        }
                    }
                }
                mutableNetworkProgress.value = false
            }.addTo(compositeDisposable)
    }

    fun retry(){
        loadProfileSubject.onNext(Unit)
    }

}