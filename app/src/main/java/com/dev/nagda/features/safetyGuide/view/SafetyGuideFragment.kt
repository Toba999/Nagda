package com.dev.nagda.features.safetyGuide.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.nagda.MainActivity
import com.dev.nagda.R
import com.dev.nagda.databinding.FragmentSafetyGuideBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SafetyGuideFragment : Fragment() {

    private var _binding: FragmentSafetyGuideBinding? = null
    private val binding get() = _binding!!

    private val tips = listOf(
        SafetyTip("حافظ على هدوئك", "لا تدع الخوف يتحكم بك وتصرف بحكمة وهدوء"),
        SafetyTip("ابتعد عن مصدر الحريق", "احتمِ بمسافة آمنة من أعمدة النيران والدخان"),
        SafetyTip("اتجه لأقرب مخرج", "استخدم مخارج الطوارئ المحددة"),
        SafetyTip("لا تستخدم المصعد", "استخدم السلالم فقط عند الإخلاء"),
        SafetyTip("تنفس من الدخان", "انحنِ واستخدم قطعة قماش مبللة على أنفك"),
        SafetyTip("ساعد الآخرين", "ساعد الأطفال وكبار السن إذا أمكن"),
        SafetyTip("اتصل بالطوارئ", "اتصل بالدفاع المدني (998) أو استخدم تطبيق نجدة"),
        SafetyTip("أغلق الأبواب خلفك", "أغلق الأبواب لمنع انتشار النيران والدخان")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSafetyGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setStausBarColor(R.color.screenBackground)
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.rvTips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SafetyTipAdapter(tips)
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCivilDefense.setOnClickListener {
            dialNumber("998")
        }
        binding.btnAmbulance.setOnClickListener {
            dialNumber("997")
        }
        binding.btnPolice.setOnClickListener {
            dialNumber("999")
        }
    }

    private fun dialNumber(number: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}