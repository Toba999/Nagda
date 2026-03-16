package com.dev.nagda.features.profile.viewModel

import android.accounts.AccountManager.KEY_PASSWORD
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagda.data.model.UserModel
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.features.login.domain.model.BiometricLoginState
import com.dev.nagda.features.login.domain.model.LoginState
import com.dev.nagda.features.profile.view.ProfileState
import com.dev.nagda.features.profile.view.SignOutState
import com.dev.nagda.features.profile.view.UpdateState
import com.dev.nagda.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: FireBaseRepo,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> = _signOutState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            repo.getProfile()
                .onSuccess { _profileState.value = ProfileState.Success(it) }
                .onFailure { _profileState.value = ProfileState.Error(it.message ?: "حدث خطأ") }
        }
    }

    fun updateProfile(user: UserModel) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            repo.updateProfile(user)
                .onSuccess { _updateState.value = UpdateState.Success }
                .onFailure { _updateState.value = UpdateState.Error(it.message ?: "حدث خطأ") }
        }
    }

    fun resetUpdateState() { _updateState.value = UpdateState.Idle }
    fun resetSignOutState() { _signOutState.value = SignOutState.Idle }
}

