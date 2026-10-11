package com.rama77.app.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rama77.app.R
import com.rama77.app.databinding.ItemMarketCardBinding
import com.rama77.app.model.MarketItem

class MarketAdapter(
    private val items: List<MarketItem>,
    private val onPlayClick: (MarketItem) -> Unit
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(val binding: ItemMarketCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val binding = ItemMarketCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MarketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvMarketName.text = item.name
            tvMarketResult.text = "${item.openPana}-${item.jodi}-${item.closePana}"
            tvOpenTime.text = "Open Bids: ${item.openTime}"
            tvCloseTime.text = "Close Bids: ${item.closeTime}"
            tvMarketStatus.text = item.status

            if (item.status == "Closed for Today") {
                tvMarketStatus.setTextColor(Color.parseColor("#DC2626"))
                btnPlayMarket.setBackgroundResource(R.drawable.bg_play_button_disabled)
            } else {
                tvMarketStatus.setTextColor(Color.parseColor("#16A34A"))
                btnPlayMarket.setBackgroundResource(R.drawable.bg_play_button)
            }

            btnPlayMarket.setOnClickListener {
                onPlayClick(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}