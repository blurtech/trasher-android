package tech.blur.trasher.domain

data class Parcel(
    val bagType: String,
    val count: Int,
    val id: String,
    val tag: String
)