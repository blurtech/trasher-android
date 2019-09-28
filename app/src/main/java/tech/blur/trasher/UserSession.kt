package tech.blur.trasher

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class UserSession {
    private val qrCodeDataSubject = BehaviorSubject.create<String>()

    fun qrCodeDataObservable(): Observable<String> = qrCodeDataSubject
    fun qrCodeData(value: String){
        qrCodeDataSubject.onNext(value)
    }
}