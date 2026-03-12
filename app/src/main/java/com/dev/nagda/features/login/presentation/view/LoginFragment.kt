package com.dev.nagda.features.login.presentation.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.dev.nagda.features.login.domain.model.BiometricLoginState
import com.dev.nagda.features.login.domain.model.LoginState
import com.dev.nagda.features.login.presentation.viewModel.LoginViewModel
import com.dev.nagda.R
import com.dev.nagda.databinding.FragmentLoginBinding
import com.dev.nagda.utils.SharedPrefManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForBiometricLogin()

        binding.fingerprintCard.setOnClickListener {
            showBiometricPrompt()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginState.Loading -> showLoading(true)
                    is LoginState.Success -> navigateToHome()
                    is LoginState.Error -> showSnackBar(state.message,true)
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.biometricLoginState.collectLatest { state ->
                when (state) {
                    is BiometricLoginState.Loading -> showLoading(true)
                    is BiometricLoginState.Success -> {
//                        val employeeData = sharedPrefManager.getEmployeeData()
//                        viewModel.login(employeeData?.email.toString(), employeeData?.password.toString())
                    }
                    is BiometricLoginState.Error -> showSnackBar(state.message,true)
                    else -> Unit
                }
            }
        }
        binding.registerBtn.setOnClickListener{
            findNavController().navigate(R.id.RegisterFragment, null)
        }

        binding.loginButton.setOnClickListener{
            navigateToHome()
//            val email = binding.emailEt.text.toString().trim()
//            val password = binding.passwordEt.text.toString().trim()
//            if (validateInputs(email, password)) {
//                viewModel.login(email, password)
//            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showSnackBar("البريد الإلكتروني مطلوب", true)
                false
            }
            password.isEmpty() -> {
                showSnackBar( "كلمة المرور مطلوبة", true)
                false
            }
            !isValidEmail(email) -> {
                showSnackBar( "الرجاء إدخال بريد إلكتروني صحيح", true)
                false
            }
            else -> true
        }
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }
    fun navigateToHome() {
        showLoading(false)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.LoginFragment, true)
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(R.id.containerFragment, null, navOptions)
    }

    private fun showSnackBar( message: String, isError: Boolean) {
        showLoading(false)
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(resources.getColor(R.color.red, null))
            snackBar.setTextColor(resources.getColor(R.color.white, null))
        }
        snackBar.show()
    }

    private fun showLoading(isShown: Boolean) {
        binding.loadingView.root.isVisible = isShown
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkForBiometricLogin() {
//        val employeeData = sharedPrefManager.getEmployeeData()
//        val isBiometricEnabled = generateKeyForBiometricAuthentication()
//
//        if (employeeData != null ) {
//            val biometricManager = BiometricManager.from(requireContext())
//            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
//                BiometricManager.BIOMETRIC_SUCCESS && isBiometricEnabled) {
//                binding.fingerprintCard.isVisible = true
//                binding.fingerPrintText.isVisible = true
//                showBiometricPrompt()
//            }else{
//                showSnackBar( "يرجي تفعيل تسجيل الدخول بالبصمة في جهازك", true)
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showBiometricPrompt() {
        val executor: Executor = ContextCompat.getMainExecutor(requireContext())

        val biometricPrompt = BiometricPrompt(this@LoginFragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
//                    viewModel.checkForBiometricLogin(sharedPrefManager.getEmployeeData()?.id.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showSnackBar( "فشل التحقق من بصمة الإصبع", true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showSnackBar( "خطأ: $errString", true)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("تسجيل الدخول ببصمة الإصبع")
            .setSubtitle("استخدم بصمة إصبعك لتسجيل الدخول")
            .setNegativeButtonText("إلغاء")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}