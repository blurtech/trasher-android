package tech.blur.trasher.domain

class LoginResponse(val user: User, val token: String) {
    fun getToken() = Token(token)
}