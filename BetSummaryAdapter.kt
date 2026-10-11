package com.rama77.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rama77.app.databinding.ItemBetSummaryBinding
import com.rama77.app.model.BetItem

class BetSummaryAdapter(
    private val items: List<BetItem>,
    private val onDeleteClick: (BetItem) -> Unit
) : RecyclerView.Adapter<BetSummaryAdapter.BetSummaryViewHolder>() {

    inner class BetSummaryViewHolder(val binding: ItemBetSummaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetSummaryViewHolder {
        val binding = ItemBetSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BetSummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BetSummaryViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvBetNumber.text = item.number
        holder.binding.tvBetPoints.text = "₹${item.points}"
        holder.binding.btnDeleteBet.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}