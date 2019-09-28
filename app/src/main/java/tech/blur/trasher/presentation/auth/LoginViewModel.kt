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
import tech.blur.trasher.domain.LoginRequest
import tech.blur.trasher.presentation.BaseViewModel

class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    api: TrasherApi,
    accountRepository: AccountRepository
) : BaseViewModel() {

    val login: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val password: BehaviorSubject<String> = BehaviorSubject.create<String>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableAreRequiredFieldsFilled = MutableLiveData<Boolean>(false)
    val areRequiredFieldsFilled: LiveData<Boolean> = mutableAreRequiredFieldsFilled

    private val mutableLoginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = mutableLoginResult

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    val loginSubject = PublishSubject.create<Unit>()

    private val credentialObservable: Observable<CredentialModel> =
        Observables.combineLatest(login, password) { emailOrLogin, password ->
            CredentialModel(emailOrLogin, password)
        }

    init {
        credentialObservable
            .map { it.login.isNotBlank() && it.password.isNotEmpty() }
            .subscribe { mutableAreRequiredFieldsFilled.value = it }
            .addTo(compositeDisposable)

        loginSubject
            .withLatestFrom(credentialObservable)
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle {
                api.login(LoginRequest(it.second.login, it.second.password)).toResult()
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
                        mutableLoginResult.value = LoginResult.SUCCESS
                    }
                    is Result.Failure -> {
                        mutableLoginResult.value = LoginResult.FAILURE

                        when (it.throwable) {
                            is HttpException -> {
                                if (it.throwable.code() == 503)
                                    mutableErrorMessage.value = "Неверные данные"
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

    private data class CredentialModel(val login: String, val password: String)

    enum class LoginResult {
        SUCCESS,
        FAILURE
    }
}