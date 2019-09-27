package tech.blur.trasher.domain

data class Wrapper<T> (
    val success: Boolean,
    val data: T
)