package tech.blur.trasher.data

import android.content.SharedPreferences
import com.google.gson.Gson
import tech.blur.trasher.domain.Trashlist

class TrashRepository(
    private val sharedPreferences: SharedPreferences
) {

    var trashCans: Trashlist
        set(value) {
            sharedPreferences.edit()
                .putString(::trashCans.name, Gson().toJson(value, Trashlist::class.java))
                .apply()
        }
        get() = Gson().fromJson(
            sharedPreferences.getString(::trashCans.name, null),
            Trashlist::class.java
        )

}