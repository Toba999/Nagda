package com.dev.nagda.data.model

data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val familySize: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)