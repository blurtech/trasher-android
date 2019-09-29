package tech.blur.trasher

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import tech.blur.trasher.domain.Trashcan
import tech.blur.trasher.domain.TrashcanInfo

class UserSession {
    private val qrCodeCanDataSubject = BehaviorSubject.create<TrashcanInfo>()
    private val buildTripSubject = BehaviorSubject.create<Unit>()
    private val trashcanClickedSubject = BehaviorSubject.create<String>()
    private val pathClickedSubject = BehaviorSubject.create<Trashcan>()

    fun qrCodeCanDataObservable(): Observable<TrashcanInfo> = qrCodeCanDataSubject
    fun qrCodeCanData(value: TrashcanInfo) {
        qrCodeCanDataSubject.onNext(value)
    }

    fun buildTripObservable(): Observable<Unit> = buildTripSubject
    fun buildTrip() {
        buildTripSubject.onNext(Unit)
    }

    fun trashcanClickedObservable(): Observable<String> = trashcanClickedSubject
    fun trashcanClicked(value: String) {
        trashcanClickedSubject.onNext(value)
    }

    fun pathClickedObservable(): Observable<Trashcan> = pathClickedSubject
    fun pathClickedClicked(value: Trashcan) {
        pathClickedSubject.onNext(value)
    }
}