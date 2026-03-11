package com.dev.nagda.features.register.models

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Failure(val error: String) : RegisterState()
}