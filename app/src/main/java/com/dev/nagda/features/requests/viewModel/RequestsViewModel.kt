package com.dev.nagda.features.requests.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.features.requests.view.RequestsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val repo: FireBaseRepo
) : ViewModel() {

    private val _requestsState = MutableStateFlow<RequestsState>(RequestsState.Idle)
    val requestsState: StateFlow<RequestsState> = _requestsState.asStateFlow()

    fun getUserRequests() {
        viewModelScope.launch {
            _requestsState.value = RequestsState.Loading
            repo.getUserRequests()
                .onSuccess { requests ->
                    _requestsState.value = if (requests.isEmpty())
                        RequestsState.Empty
                    else
                        RequestsState.Success(requests)
                }
                .onFailure {
                    _requestsState.value = RequestsState.Error(it.message ?: "حدث خطأ")
                }
        }
    }
}