package com.dev.nagda.features.safetyGuide.view

import android.R.attr.divider
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.nagda.databinding.ItemSafetyTipBinding

class SafetyTipAdapter(private val items: List<SafetyTip>) :
    RecyclerView.Adapter<SafetyTipAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSafetyTipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSafetyTipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvNumber.text   = (position + 1).toString()
            tvTipTitle.text = item.title
            tvTipDesc.text  = item.description
        }
    }

    override fun getItemCount() = items.size
}

data class SafetyTip(val title: String, val description: String)