package com.rama77.app.ui.gamecenter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rama77.app.adapter.BetSummaryAdapter
import com.rama77.app.adapter.NumberInputAdapter
import com.rama77.app.adapter.SeriesTabAdapter
import com.rama77.app.databinding.ActivitySubGameBetBinding
import com.rama77.app.model.BetItem

class SubGameBetActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubGameBetBinding
    private val betsList = mutableListOf<BetItem>()
    private lateinit var betSummaryAdapter: BetSummaryAdapter
    private var totalPoints = 0

    private var marketName = "KALYAN"
    private var session = "Open"
    private var gameId = "single_digit"
    private var gameTitle = "Single Digit"
    private var isBulk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullscreen()
        binding = ActivitySubGameBetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        marketName = intent.getStringExtra("EXTRA_MARKET_NAME") ?: "KALYAN"
        session = intent.getStringExtra("EXTRA_SESSION") ?: "Open"
        gameId = intent.getStringExtra("EXTRA_GAME_ID") ?: "single_digit"
        gameTitle = intent.getStringExtra("EXTRA_GAME_TITLE") ?: "Single Digit"
        isBulk = intent.getBooleanExtra("EXTRA_IS_BULK", false)

        binding.tvSubGameTitle.text = "$marketName - $session - $gameTitle"

        binding.btnBackSub.setOnClickListener { finish() }

        setupBetsSummary()
        setupInputGrid()
    }

    private fun enableFullscreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
        }
    }

    private fun setupBetsSummary() {
        betSummaryAdapter = BetSummaryAdapter(betsList) { item ->
            betsList.remove(item)
            updateTotal()
            betSummaryAdapter.notifyDataSetChanged()
        }

        binding.rvBetsSummary.layoutManager = LinearLayoutManager(this)
        binding.rvBetsSummary.adapter = betSummaryAdapter

        binding.tvDeleteAll.setOnClickListener {
            betsList.clear()
            updateTotal()
            betSummaryAdapter.notifyDataSetChanged()
        }

        binding.btnConfirmBets.setOnClickListener {
            if (betsList.isEmpty()) {
                Toast.makeText(this, "Please add bets before confirming.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Bid Placed Successfully! Total: ₹$totalPoints", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateTotal() {
        totalPoints = betsList.sumOf { it.points }
        binding.tvTotalPoints.text = "₹$totalPoints"
    }

    private fun setupInputGrid() {
        val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        binding.rvNumbersGrid.layoutManager = GridLayoutManager(this, 2)
        binding.rvNumbersGrid.adapter = NumberInputAdapter(numbers) { num, points ->
            if (points > 5000) {
                Toast.makeText(
                    this,
                    "Bid for digit $num (₹$points) exceeds the maximum bid amount of ₹5000.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnAddBets.setOnClickListener {
            // Add entries from inputs to bets summary
            Toast.makeText(this, "Numbers added to bet summary", Toast.LENGTH_SHORT).show()
        }
    }
}