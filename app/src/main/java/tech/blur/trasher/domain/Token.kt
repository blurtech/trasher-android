package tech.blur.trasher.domain

data class Token (
    val token: String,
    val type: String = "Bearer"
)