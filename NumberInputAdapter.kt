package com.rama77.app.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rama77.app.databinding.ItemNumberInputBinding

class NumberInputAdapter(
    private val numbers: List<String>,
    private val onPointsChanged: (String, Int) -> Unit
) : RecyclerView.Adapter<NumberInputAdapter.NumberInputViewHolder>() {

    private val pointsMap = mutableMapOf<String, Int>()

    inner class NumberInputViewHolder(val binding: ItemNumberInputBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberInputViewHolder {
        val binding = ItemNumberInputBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NumberInputViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NumberInputViewHolder, position: Int) {
        val num = numbers[position]
        holder.binding.tvNumberBadge.text = num
        holder.binding.etPointsInput.setText(pointsMap[num]?.toString() ?: "")

        holder.binding.etPointsInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pts = s?.toString()?.toIntOrNull() ?: 0
                pointsMap[num] = pts
                onPointsChanged(num, pts)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun getItemCount(): Int = numbers.size

    fun getEnteredBets(): Map<String, Int> = pointsMap.filter { it.value > 0 }
}