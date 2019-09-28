package tech.blur.trasher.presentation.qr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.subjects.PublishSubject
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Parcel
import tech.blur.trasher.presentation.BaseViewModel

class QRScanerViewModel(
    api: TrasherApi,
    schedulerProvider: SchedulerProvider
): BaseViewModel() {

    val registerParcelsSubject = PublishSubject.create<Parcel>()

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val mutableRegistrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = mutableRegistrationResult

    init {

    }

    //TODO Добавление пакета
}