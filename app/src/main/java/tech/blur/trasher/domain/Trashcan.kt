package tech.blur.trasher.domain

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Trashcan(
    @SerializedName("_id")
    val id: String,
    val place: Place,
    val lastCollect: String,
    val latlng: LatLng

)

data class Place(val city: String, val region: String)

data class Trashlist(val list: List<Trashcan>)

data class TrashcanInfo(
    val type: Int,
    val count: Int,
    val id: String,
    val tag: String
)

enum class TrashcanType(id: Int){
    PAPAER(1),
    GLASS(2),
    PLASTIC(3),
    METAL(4),
    CLOUTHES(5),
    OTHER(6),
    DANGER(7),
    BATTARIES(8),
    BULBS(9),
    TECHNIC(10),
    TETRA(11)
}

data class req(
    val type: Byte,
    val storage: String,
    val mass: Int,
    val bag: Int
)