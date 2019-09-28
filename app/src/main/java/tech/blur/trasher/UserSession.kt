package tech.blur.trasher

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import tech.blur.trasher.domain.TrashcanInfo

class UserSession {
    private val qrCodeCanDataSubject = BehaviorSubject.create<TrashcanInfo>()

    fun qrCodeCanDataObservable(): Observable<TrashcanInfo> = qrCodeCanDataSubject
    fun qrCodeCanData(value: TrashcanInfo){
        qrCodeCanDataSubject.onNext(value)
    }
}