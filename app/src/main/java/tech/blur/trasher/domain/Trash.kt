package tech.blur.trasher.domain

import android.util.SparseArray
import com.google.gson.annotations.SerializedName

data class Trash(
    @SerializedName("_id") val id: String,
    val types: SparseArray<Count>,
    val throwDate: String,
    val client: String,
    val storage: String
)

data class Count(
    val mass: Int,
    val bag: Int
)

data class EjectTrashRequest(
    val storage: String,
    val types: SparseArray<Count>
)