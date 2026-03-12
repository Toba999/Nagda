package com.dev.nagda.features.home.viewModel

import com.dev.nagda.features.login.domain.model.LoginState


sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val user: String?) : HomeState()
    data class Error(val message: String) : HomeState()
}