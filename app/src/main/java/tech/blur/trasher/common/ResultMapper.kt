package tech.blur.trasher.common

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@CheckResult
fun <T> Flowable<T>.toResult(): Flowable<Result<T>> {
    return compose { item ->
        item
            .map { Result.success(it) }
            .onErrorReturn { e -> Result.failure(e) }
    }
}

@CheckResult
fun <T> Observable<T>.toResult(): Observable<Result<T>> {
    return compose { item ->
        item
            .map { Result.success(it) }
            .onErrorReturn { e -> Result.failure(e) }
    }
}

@CheckResult
fun <T> Single<T>.toResult(): Single<Result<T>> {
    return compose { item ->
        item
            .map { Result.success(it) }
            .onErrorReturn { e -> Result.failure(e) }
    }
}

@CheckResult
fun Completable.toResult(): Single<Result<Unit>> {
    return toSingleDefault(Unit).toResult()
}