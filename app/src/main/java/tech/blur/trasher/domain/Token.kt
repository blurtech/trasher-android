package tech.blur.trasher.domain

data class Token (
    val type: String = "Bearer",
    val token: String
)