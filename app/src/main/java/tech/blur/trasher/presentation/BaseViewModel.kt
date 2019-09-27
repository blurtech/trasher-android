package tech.blur.trasher.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    protected val mutableRequestFailedWithNoInternet = MutableLiveData<Boolean>()
    val requestFailedWithNoInternet: LiveData<Boolean> = mutableRequestFailedWithNoInternet

    fun launch(job: () -> Disposable) {
        compositeDisposable.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}