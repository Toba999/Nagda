package com.dev.nagda.features.addRequest.viewModel

sealed class SendRequestState {
    object Idle    : SendRequestState()
    object Loading : SendRequestState()
    object Success : SendRequestState()
    data class Error(val message: String) : SendRequestState()
}