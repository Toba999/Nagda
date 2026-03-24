package com.dev.nagda.features.home.viewModel

import androidx.lifecycle.ViewModel
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.features.login.domain.model.BiometricLoginState
import com.dev.nagda.features.login.domain.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val firebaseRepository: FireBaseRepo) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState: StateFlow<HomeState> = _homeState
}