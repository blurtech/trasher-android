package tech.blur.trasher.domain

data class RegisterRequest(
    val username: String,
    val password: String,
    val address: Address
)
