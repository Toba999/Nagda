package com.dev.nagda.features.addRequest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagda.data.model.RequestModel
import com.dev.nagda.domain.repo.FireBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRequestViewModel @Inject constructor(
    private val repo: FireBaseRepo
) : ViewModel() {

    private val _sendState = MutableStateFlow<SendRequestState>(SendRequestState.Idle)
    val sendState: StateFlow<SendRequestState> = _sendState.asStateFlow()

    fun sendRequest(request: RequestModel) {
        viewModelScope.launch {
            _sendState.value = SendRequestState.Loading
            repo.sendRequest(request)
                .onSuccess { _sendState.value = SendRequestState.Success }
                .onFailure { _sendState.value = SendRequestState.Error(
                    it.message ?: "حدث خطأ أثناء إرسال البلاغ") }
        }
    }
}
