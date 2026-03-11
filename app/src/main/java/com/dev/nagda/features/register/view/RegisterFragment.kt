package com.dev.nagda.features.register.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dev.nagda.R
import com.dev.nagda.databinding.FragmentRegisterBinding
import com.dev.nagda.features.register.models.RegisterState
import com.dev.nagda.features.register.viewModel.RegisterViewModel
import com.dev.nagda.utils.SharedPrefManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    private var latitude : Double? = null
    private var longitude : Double? = null
    private var address : String? = null

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
        parentFragmentManager.setFragmentResultListener("locationRequestKey", viewLifecycleOwner) { _, bundle ->
            latitude = bundle.getDouble("latitude")
            longitude = bundle.getDouble("longitude")
            address = bundle.getString("address")
//            binding.adminLocation.setText(address)
        }
    }


    private fun setupListeners() {
        binding.registerBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

//        binding.adminLocation.setOnClickListener {
//            findNavController().navigate(R.id.action_registerEmployeeFragment_to_mapFragment)
//        }

        binding.registerButton.setOnClickListener {}
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

//    private fun validateInputs(isAdmin: Boolean): Boolean {
//        var isValid = true
//
//        val name = binding.adminName.text.toString().trim()
//        val email = binding.adminEmail.text.toString().trim()
//        val password = binding.adminPassword.text.toString().trim()
//        val area = binding.locationArea.text.toString()
//
//
//        // Validate name (Required for both admin & employee)
//        if (name.isEmpty()) {
//            binding.adminName.error = "الاسم مطلوب"
//            isValid = false
//        } else {
//            binding.adminName.error = null
//        }
//
//        // Validate email (Required for both admin & employee)
//        if (email.isEmpty()) {
//            binding.adminEmail.error = "البريد الإلكتروني مطلوب"
//            isValid = false
//        } else if (!isValidEmail(email)) {
//            binding.adminEmail.error = "البريد الإلكتروني غير صالح"
//            isValid = false
//        } else {
//            binding.adminEmail.error = null
//        }
//
//        // Validate password (Required for both admin & employee)
//        if (password.isEmpty()) {
//            binding.adminPassword.error = "كلمة المرور مطلوبة"
//            isValid = false
//        } else if (password.length < 6) {
//            binding.adminPassword.error = "يجب أن تحتوي كلمة المرور على 6 أحرف على الأقل"
//            isValid = false
//        } else {
//            binding.adminPassword.error = null
//        }
//
//        // If the user is an admin, return the result without checking other fields
//        if (isAdmin) return isValid
//
//
//        // Validate area (Required only for employees)
//        if (area.isEmpty()) {
//            binding.locationArea.error = "المساحة مطلوبة"
//            isValid = false
//        } else {
//            binding.locationArea.error = null
//        }
//
//
//
//        return isValid
//    }





    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collectLatest { state ->
                when (state) {
                    is RegisterState.Loading -> {
                        showLoading(true)
                    }
                    is RegisterState.Success -> {
//                        navigateToContainer(state.message)
                    }
                    is RegisterState.Failure -> {
                        showLoading(false)
                        showSnackBar(requireView(), state.error, true)
                    }
                    is RegisterState.Idle -> {}
                }
            }
        }

    }

//    private fun navigateToContainer(message: String) {
//        if (isAdmin == true){
//            showSnackBar(requireView(),message,false)
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.registerFragment, true)
//                .setLaunchSingleTop(true)
//                .build()
//            findNavController().navigate(R.id.containerFragment, null, navOptions)
//        }else{
//            showSnackBar(requireView(),message,false)
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.registerFragment, true)
//                .setLaunchSingleTop(true)
//                .build()
//            findNavController().navigate(R.id.action_registerEmployeeFragment_to_homeFragment, null, navOptions)
//        }
//
//    }
    private fun showSnackBar(view: View, message: String, isError: Boolean) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(resources.getColor(R.color.red, null))
            snackBar.setTextColor(resources.getColor(R.color.white, null))
        }else{
            snackBarView.setBackgroundColor(resources.getColor(R.color.green, null))
            snackBar.setTextColor(resources.getColor(R.color.white, null))
        }
        snackBar.show()
    }
    private fun showLoading(isShown: Boolean) {
        binding.loadingView.root.isVisible = isShown
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}