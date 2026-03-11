package com.dev.nagda.features.onboarding.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.dev.nagda.R
import com.dev.nagda.databinding.FragmentOnboardingBinding
import com.dev.nagda.utils.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    private val screens = listOf(
        OnboardingData(R.drawable.ic_lightning,
            "بلاغ سريع للطوارئ", "أرسل بلاغك للدفاع المدني بضغطة زر واحدة"),
        OnboardingData(R.drawable.ic_location,
            "موقعك يوصل فورًا", "يتم تحديد موقعك وإرساله تلقائيًا مع البلاغ"),
        OnboardingData(R.drawable.ic_bell,
            "بدون مكالمات أو شرح", "بياناتك المحفوظة تصل مباشرة لفريق الإنقاذ")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = OnboardingPagerAdapter(screens)
        binding.viewPager.layoutDirection = View.LAYOUT_DIRECTION_RTL
        binding.viewPager.isUserInputEnabled = false
        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }

        binding.viewPager.post {
            setupDots(0)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
                binding.btnNext.text = if (position == screens.lastIndex) "ابدأ الآن" else "التالي"
            }
        })

        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < screens.lastIndex)
                binding.viewPager.currentItem++
            else
                finishOnboarding()
        }

        binding.btnSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        sharedPrefManager.putBoolean("onboarding_done", true)
        findNavController().navigate(R.id.LoginFragment)
    }

    private fun setupDots(selected: Int) {
        binding.dotsLayout.removeAllViews()
        screens.forEachIndexed { index, _ ->
            val dot = View(requireContext())
            val w = if (index == selected) 20.dp else 8.dp
            dot.layoutParams = LinearLayout.LayoutParams(w, 8.dp).apply {
                setMargins(4.dp, 0, 4.dp, 0)
            }
            dot.background = ContextCompat.getDrawable(
                requireContext(),
                if (index == selected) R.drawable.dot_active else R.drawable.dot_inactive
            )
            binding.dotsLayout.addView(dot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val Int.dp get() = (this * resources.displayMetrics.density).toInt()
}