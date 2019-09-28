package tech.blur.trasher.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.blur.trasher.data.AccountRepository

class MainActivityViewModel(
    accountRepository: AccountRepository
): BaseViewModel(){

    private val mutableValidationStatus = MutableLiveData<Boolean>()
    val validationStatus: LiveData<Boolean> = mutableValidationStatus

    fun validateUser(){

    }

}
