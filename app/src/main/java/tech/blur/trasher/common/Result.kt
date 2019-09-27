package tech.blur.trasher.common

sealed class Result<T> {
    data class Success<T>(var data: T) : Result<T>()
    data class Failure<T>(val throwable: Throwable) : Result<T>()
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(throwable: Throwable): Result<T> = Failure(throwable)
    }
}