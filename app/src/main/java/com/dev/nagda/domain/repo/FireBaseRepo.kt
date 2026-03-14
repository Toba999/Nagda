package com.dev.nagda.domain.repo

import com.dev.nagda.data.model.UserModel


interface FireBaseRepo {
    suspend fun register(user: UserModel, password: String): Result<Unit>
    suspend fun login(phone: String, password: String): Result<UserModel>
    suspend fun getProfile(): Result<UserModel>
    suspend fun updateProfile(user: UserModel): Result<Unit>
}