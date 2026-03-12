package com.dev.nagda.features.home.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
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
import com.dev.nagda.databinding.FragmentHomeBinding
import com.dev.nagda.databinding.FragmentLoginBinding
import com.dev.nagda.features.home.viewModel.HomeState
import com.dev.nagda.features.home.viewModel.HomeViewModel
import com.dev.nagda.utils.SharedPrefManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.screenBackground)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeState.collectLatest { state ->
                when (state) {
                    is HomeState.Loading -> showLoading(true)
                    is HomeState.Success -> {}
                    is HomeState.Error -> showSnackBar(state.message,true)
                    else -> Unit
                }
            }
        }


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


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        _binding = null
    }
}