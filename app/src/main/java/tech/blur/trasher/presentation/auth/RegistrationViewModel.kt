package tech.blur.trasher.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Address
import tech.blur.trasher.domain.RegisterRequest
import tech.blur.trasher.domain.User
import tech.blur.trasher.presentation.BaseViewModel

class RegistrationViewModel(
    schedulerProvider: SchedulerProvider,
    accountRepository: AccountRepository,
    api: TrasherApi
) : BaseViewModel() {
    val login: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val password: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val city: BehaviorSubject<String> = BehaviorSubject.create<String>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableAreRequiredFieldsFilled = MutableLiveData<Boolean>(false)
    val areRequiredFieldsFilled: LiveData<Boolean> = mutableAreRequiredFieldsFilled

    private val mutableRegistrationResult = MutableLiveData<User?>()
    val registrationResult: LiveData<User?> = mutableRegistrationResult

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    val loginSubject = PublishSubject.create<Unit>()

    private val registrationDataObservable: Observable<RegistrationModel> =
        Observables.combineLatest(login, password, city) { emailOrLogin, password, city ->
            RegistrationModel(emailOrLogin, password, city)
        }

    init {
        registrationDataObservable
            .map { it.login.isNotBlank() && it.password.isNotEmpty() && it.city.isNotEmpty() }
            .subscribe { mutableAreRequiredFieldsFilled.value = it }
            .addTo(compositeDisposable)

        loginSubject
            .withLatestFrom(registrationDataObservable)
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle {
                api.register(
                    RegisterRequest(
                        it.second.login,
                        it.second.password,
                        Address(it.second.city)
                    )
                ).toResult()
            }
            .flatMapSingle {
                if (it is Result.Success) {
                    accountRepository.authorizeUser(it.data.data.user, it.data.data.getToken())
                }
                Single.just(it)
            }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableRegistrationResult.value = it.data.data.user
                    }
                    is Result.Failure -> {
                        mutableRegistrationResult.value = null
                        when (it.throwable) {
                            is HttpException -> {
                                if (it.throwable.code() == 422)
                                    mutableErrorMessage.value =
                                        "Пользователь с таким логином уже существует"
                                else
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

    private data class RegistrationModel(val login: String, val password: String, val city: String)
}