package tech.blur.trasher.domain

data class User(
    val id: String,
    val username: String,
    val bags: Int,
    val points: Int,
    val address: Address
)
//
//data class UserAddress(
//    val city =
//)