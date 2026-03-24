package com.dev.nagda.features.profile.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dev.nagda.MainActivity
import com.dev.nagda.R
import com.dev.nagda.data.model.UserModel
import com.dev.nagda.databinding.FragmentProfileBinding
import com.dev.nagda.databinding.FragmentSafetyGuideBinding
import com.dev.nagda.features.profile.viewModel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setStausBarColor(R.color.screenBackground)
        viewModel.getProfile()
        observeStates()
        setupListeners()
        setEditMode(false)
    }

    private fun setupListeners() {
        binding.btnEdit.setOnClickListener {
            setEditMode(!isEditMode)
        }
    }

    private fun setEditMode(enabled: Boolean) {
        isEditMode = enabled

        val fields = listOf(
            binding.etName,
            binding.etPhone,
            binding.etEmail,
            binding.etAddress,
            binding.etFamilySize,
            binding.etNotes
        )

        fields.forEach { field ->
            field.isEnabled   = enabled
            field.isFocusable = enabled
            field.isFocusableInTouchMode = enabled
        }

        if (enabled){
            binding.btnSave.text = getString(R.string.save)
            binding.btnSave.backgroundTintList = ColorStateList.valueOf(
                getColor(requireContext(), R.color.color_call)
            )
            binding.btnSave.setOnClickListener {
                if (!validateInputs()) return@setOnClickListener
                binding.tvProfileName.text = binding.etName.text.toString().trim()
                binding.tvProfilePhone.text = binding.etPhone.text.toString().trim()
                val user = UserModel(
                    fullName   = binding.etName.text.toString().trim(),
                    phone      = binding.etPhone.text.toString().trim(),
                    mail       = binding.etEmail.text.toString().trim(),
                    address    = binding.etAddress.text.toString().trim(),
                    familySize = binding.etFamilySize.text.toString().toIntOrNull() ?: 0,
                    notes      = binding.etNotes.text.toString().trim()
                )
                viewModel.updateProfile(user)
            }
        } else {
            binding.btnSave.text = getString(R.string.signOut)
            binding.btnSave.backgroundTintList = ColorStateList.valueOf(
                getColor(requireContext(), R.color.primary)
            )
            binding.btnSave.setOnClickListener {
                findNavController().navigate(R.id.logoutDialogFragment)
            }
        }

        binding.btnEdit.isVisible = !enabled

        if (enabled) binding.etName.requestFocus()
    }

    private fun validateInputs(): Boolean {
        val name       = binding.etName.text.toString().trim()
        val phone      = binding.etPhone.text.toString().trim()
        val mail       = binding.etEmail.text.toString().trim()
        val address    = binding.etAddress.text.toString().trim()
        val familySize = binding.etFamilySize.text.toString().trim()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        return when {
            name.isEmpty()            -> { showSnackBar("الاسم مطلوب", true); false }
            phone.isEmpty()           -> { showSnackBar("رقم الهاتف مطلوب", true); false }
            phone.length < 10         -> { showSnackBar("رقم الهاتف غير صحيح", true); false }
            mail.isEmpty()            -> { showSnackBar("البريد الإلكتروني مطلوب", true); false }
            !mail.matches(emailRegex) -> { showSnackBar("البريد الإلكتروني غير صحيح", true); false }
            address.isEmpty()         -> { showSnackBar("عنوان المنزل مطلوب", true); false }
            familySize.isEmpty()      -> { showSnackBar("عدد أفراد العائلة مطلوب", true); false }
            else -> true
        }
    }

    private fun observeStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profileState.collectLatest { state ->
                when (state) {
                    is ProfileState.Loading -> showLoading(true)
                    is ProfileState.Success -> {
                        showLoading(false)
                        bindProfile(state.user)
                    }
                    is ProfileState.Error -> {
                        showLoading(false)
                        showSnackBar(state.message, true)
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateState.collectLatest { state ->
                when (state) {
                    is UpdateState.Loading -> showLoading(true)
                    is UpdateState.Success -> {
                        showLoading(false)
                        showSnackBar("تم حفظ البيانات بنجاح", false)
                        setEditMode(false)
                        viewModel.resetUpdateState()
                    }
                    is UpdateState.Error -> {
                        showLoading(false)
                        showSnackBar(state.message, true)
                        viewModel.resetUpdateState()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signOutState.collectLatest { state ->
                when (state) {
                    is SignOutState.Loading -> showLoading(true)
                    is SignOutState.Success -> {
                        navigateToLogin()
                        viewModel.resetSignOutState()
                    }
                    is SignOutState.Error   -> {
                        showLoading(false)
                        showSnackBar(state.message, true)
                        viewModel.resetSignOutState()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun navigateToLogin() {
        showLoading(false)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.containerFragment, true)
            .setLaunchSingleTop(true)
            .build()
        requireActivity().findNavController(R.id.nav_host_fragment)
            .navigate(R.id.LoginFragment, null, navOptions)
    }
    private fun bindProfile(user: UserModel?) {
        binding.tvProfileName.text  = user?.fullName
        binding.tvProfilePhone.text = user?.phone
        binding.etName.setText(user?.fullName)
        binding.etPhone.setText(user?.phone)
        binding.etEmail.setText(user?.mail)
        binding.etAddress.setText(user?.address)
        binding.etFamilySize.setText(user?.familySize.toString())
        binding.etNotes.setText(user?.notes)
    }

    private fun showSnackBar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            resources.getColor(if (isError) R.color.red else R.color.green, null)
        )
        snackBar.setTextColor(resources.getColor(R.color.white, null))
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