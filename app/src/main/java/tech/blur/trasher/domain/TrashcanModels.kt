package tech.blur.trasher.domain

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Trashcan(
    @SerializedName("_id")
    val id: String,
    val place: Place,
    val lastCollect: String,
    val latlng: LatLng,
    val title: String

)

data class Place(val city: String, val region: String)

data class Trashlist(val list: List<Trashcan>)

data class TrashcanInfo(
    val canType: Int,
    val id: String
)

enum class TrashcanType(id: Int) {
    PAPER(1),
    GLASS(2),
    PLASTIC(3),
    METAL(4),
    CLOUTHES(5),
    OTHER(6),
    DANGER(7),
    BATTARIES(8),
    BULBS(9),
    TECHNIC(10),
    TETRA(11);

    fun isOther(): Boolean {
        return when(this) {
            PAPER, GLASS, PLASTIC, DANGER -> true
            else -> false
        }
    }
}

data class req(
    val type: Byte,
    val storage: String,
    val mass: Int,
    val bag: Int
)