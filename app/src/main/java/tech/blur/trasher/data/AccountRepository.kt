package tech.blur.trasher.data

import android.content.SharedPreferences
import com.google.gson.Gson
import tech.blur.trasher.domain.Token
import tech.blur.trasher.domain.User

class AccountRepository(
    private val sharedPreferences: SharedPreferences
) {

    var userToken: Token
        private set(token) {
            sharedPreferences.edit()
                .putString(::userToken.name, Gson().toJson(token, Token::class.java))
                .apply()
        }
        get() = Gson().fromJson(
            sharedPreferences.getString(::userToken.name, null),
            Token::class.java
        )

    val isUserLoggedIn: Boolean
        get() = sharedPreferences.getString(::userToken.name, null) != null

    var user: User
        private set(value) {
            sharedPreferences.edit()
                .putString(::user.name, Gson().toJson(value, User::class.java))
                .apply()
        }
        get() = Gson().fromJson(sharedPreferences.getString(::user.name, null), User::class.java)

    fun authorizeUser(user: User, token: Token) {
        if (token.token.isBlank()) throw IllegalStateException("Token must be provided")
        this.user = user
        this.userToken = token
    }

    fun logOut() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}