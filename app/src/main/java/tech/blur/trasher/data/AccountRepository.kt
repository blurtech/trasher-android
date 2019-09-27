package tech.blur.trasher.data

import android.content.SharedPreferences
import com.google.gson.Gson
import tech.blur.trasher.domain.Token

class AccountRepository(
    private val sharedPreferences: SharedPreferences
) {

    var userToken: Token
        set(token) {
            sharedPreferences.edit()
                .putString(::userToken.name, Gson().toJson(token, Token::class.java))
                .apply()
        }
        get() = Gson().fromJson(sharedPreferences.getString(::userToken.name, null), Token::class.java)

    val isUserLoggedIn: Boolean
        get() = sharedPreferences.getString(::userToken.name, null) != null
}