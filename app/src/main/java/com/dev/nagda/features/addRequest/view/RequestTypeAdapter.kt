package com.dev.nagda.features.addRequest.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dev.nagda.R
import com.dev.nagda.data.model.RequestType
import com.dev.nagda.databinding.ItemRequestTypeBinding

class RequestTypeAdapter(
    private val items: List<RequestType>,
    private val onTypeSelected: (RequestType) -> Unit
) : RecyclerView.Adapter<RequestTypeAdapter.ViewHolder>() {

    private var selectedType: RequestType? = null

    class ViewHolder(val binding: ItemRequestTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRequestTypeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = items[position]
        with(holder.binding) {
            tvLabel.text = type.label
            ivIcon.setImageResource(type.iconRes)

            val isSelected = type == selectedType
            ivCheck.isVisible = isSelected
            ivIcon.setBackgroundResource(
                if (isSelected) R.drawable.circle_request_type
                else R.drawable.circle_gray_bg
            )

            root.setOnClickListener {
                selectedType = type
                notifyDataSetChanged()
                onTypeSelected(type)
            }
        }
    }

    override fun getItemCount() = items.size
}