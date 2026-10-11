package com.rama77.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rama77.app.databinding.ItemGameCenterOptionBinding
import com.rama77.app.model.GameOption

class GameOptionAdapter(
    private val options: List<GameOption>,
    private val onItemClick: (GameOption) -> Unit
) : RecyclerView.Adapter<GameOptionAdapter.GameOptionViewHolder>() {

    inner class GameOptionViewHolder(val binding: ItemGameCenterOptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOptionViewHolder {
        val binding = ItemGameCenterOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GameOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameOptionViewHolder, position: Int) {
        val option = options[position]
        holder.binding.tvGameTitle.text = option.title
        holder.binding.ivGameIcon.setImageResource(option.iconResId)
        holder.itemView.setOnClickListener {
            onItemClick(option)
        }
    }

    override fun getItemCount(): Int = options.size
}