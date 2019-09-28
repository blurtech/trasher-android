package tech.blur.trasher.domain

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Trashcan(
    @SerializedName("_id")
    val id: String,
    val place: Place,
    val lastCollect: String,
    val position: LatLng

)

data class Place(val city: String,  val region: String)
