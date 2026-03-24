package com.dev.nagda.features.addRequest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.nagda.MainActivity
import com.dev.nagda.R
import com.dev.nagda.data.model.RequestModel
import com.dev.nagda.data.model.RequestType
import com.dev.nagda.databinding.FragmentAddRequestBinding
import com.dev.nagda.features.addRequest.viewModel.AddRequestViewModel
import com.dev.nagda.features.addRequest.viewModel.SendRequestState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddRequestFragment : Fragment() {

    private var _binding: FragmentAddRequestBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddRequestViewModel by viewModels()

    private lateinit var typeAdapter: RequestTypeAdapter

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setStausBarColor(R.color.screenBackground)
        setupTypeGrid()
        setupListeners()
        observeState()
        observeLocation()
    }

    private fun setupTypeGrid() {
        typeAdapter = RequestTypeAdapter(RequestType.entries) { type ->
            selectedType = type
        }
        binding.rvTypes.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = typeAdapter
        }
        typeAdapter.setSelected(selectedType ?: RequestType.ACCIDENT)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.locationCard.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }
        binding.btnSend.setOnClickListener {
            val type = selectedType
            if (type == null) {
                showSnackBar("يرجى اختيار نوع البلاغ"); return@setOnClickListener
            }
            if (address.isNullOrEmpty()) {
                showSnackBar("يرجى تحديد الموقع"); return@setOnClickListener
            }
            val request = RequestModel(
                type = type,
                location = address ?: "",
                latitude = latitude ?: 0.0,
                longitude = longitude ?: 0.0,
                details = binding.etDetails.text.toString().trim()
            )
            viewModel.sendRequest(request)
        }
    }

    private fun observeLocation() {
        parentFragmentManager.setFragmentResultListener("locationRequestKey", viewLifecycleOwner) { _, bundle ->
            latitude = bundle.getDouble("latitude")
            longitude = bundle.getDouble("longitude")
            address = bundle.getString("address")
            binding.tvLocation.text = address
            binding.tvChange.isVisible = !address.isNullOrEmpty()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sendState.collectLatest { state ->
                when (state) {
                    is SendRequestState.Loading -> showLoading(true)
                    is SendRequestState.Success -> {
                        showLoading(false)
                        findNavController().navigate(R.id.successDialogFragment)
                    }
                    is SendRequestState.Error -> {
                        showLoading(false)
                        showSnackBar(state.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.btnSend.isEnabled = !show
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(resources.getColor(R.color.red, null))
            setTextColor(resources.getColor(R.color.white, null))
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        var selectedType: RequestType? = null
    }
}