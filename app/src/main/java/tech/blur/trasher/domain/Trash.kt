package tech.blur.trasher.domain

import com.google.gson.annotations.SerializedName

data class Trash(
    @SerializedName("_id") val id: String,
    val types: ArrayList<Count>,
    val throwDate: String,
    val client: String,
    val storage: String
)

data class Count(
    val bagtype: Int,
    val mass: Int,
    val bags: Int
)

data class EjectTrashRequest(
    val storage: String,
    val types: ArrayList<Count>
)