package com.dev.nagda.features.requestDetails.viewModel

import com.dev.nagda.data.model.RequestModel

sealed class RequestDetailsState {
    object Idle    : RequestDetailsState()
    object Loading : RequestDetailsState()
    data class Success(val request: RequestModel) : RequestDetailsState()
    data class Error(val message: String)         : RequestDetailsState()
}