package tech.blur.trasher.domain

import com.google.gson.annotations.SerializedName

data class Statistic(
   @SerializedName("_id") val type: Int,
   val value: Value
)

data class Value(
    val total: Int
)