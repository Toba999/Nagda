package com.dev.nagda.features.onboarding.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.nagda.databinding.ItemOnboardingPageBinding

class OnboardingPagerAdapter(items: List<OnboardingData>) :
    RecyclerView.Adapter<OnboardingPagerAdapter.ViewHolder>() {

    private val reversedItems = items

    class ViewHolder(val binding: ItemOnboardingPageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOnboardingPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reversedItems[position]
        with(holder.binding) {
            imgIcon.setImageResource(item.iconRes)
            tvTitle.text = item.title
            tvDesc.text = item.description
        }
    }

    override fun getItemCount() = reversedItems.size
}