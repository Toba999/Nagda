package com.dev.nagda.features.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.features.login.domain.model.BiometricLoginState
import com.dev.nagda.features.login.domain.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseRepository: FireBaseRepo) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _biometricLoginState = MutableStateFlow<BiometricLoginState>(BiometricLoginState.Idle)
    val biometricLoginState: StateFlow<BiometricLoginState> = _biometricLoginState


//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _loginState.value = LoginState.Loading
//            try {
//                val user = firebaseRepository.loginEmployee(email, password)
//                if (user != null) {
//                    _loginState.value = LoginState.Success(user)
//                } else {
//                    _loginState.value = LoginState.Error("البريد الاكتروني او كلمة المرور غير صحيحة")
//                }
//            } catch (e: Exception) {
//                _loginState.value = LoginState.Error(e.message ?: "حدث خطأ أثناء تسجيل الدخول")
//            }
//        }
//    }
//    fun checkForBiometricLogin(employeeId: String) {
//        viewModelScope.launch {
//            _biometricLoginState.value = BiometricLoginState.Loading
//            try {
//                val isExist = firebaseRepository.isEmployeeExist(employeeId)
//                if (isExist) {
//                    _biometricLoginState.value = BiometricLoginState.Success("تم تسجيل الدخول بنجاح")
//                } else {
//                    _biometricLoginState.value = BiometricLoginState.Error("تم مسح بيانات الموظف")
//                }
//            } catch (e: Exception) {
//                _biometricLoginState.value = BiometricLoginState.Error(e.message ?: "حدث خطأ أثناء تسجيل الدخول")
//            }
//        }
//    }
}