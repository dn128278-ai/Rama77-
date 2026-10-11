package com.rama77.app.ui.gamecenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.rama77.app.R
import com.rama77.app.adapter.GameOptionAdapter
import com.rama77.app.databinding.ActivityGameCenterBinding
import com.rama77.app.model.GameOption

class GameCenterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameCenterBinding
    private var currentSession = "Open"
    private var marketName = "KALYAN"
    private var openTime = ""
    private var closeTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullscreen()
        binding = ActivityGameCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        marketName = intent.getStringExtra("EXTRA_MARKET_NAME") ?: "KALYAN"
        openTime = intent.getStringExtra("EXTRA_OPEN_TIME") ?: "09:25 PM"
        closeTime = intent.getStringExtra("EXTRA_CLOSE_TIME") ?: "11:30 PM"

        binding.tvMarketTitle.text = marketName
        binding.tvMarketTiming.text = "$openTime - $closeTime"

        binding.btnBack.setOnClickListener { finish() }

        setupSessionTabs()
        setupGameOptionsGrid()
    }

    private fun enableFullscreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
        }
    }

    private fun setupSessionTabs() {
        binding.btnSessionOpen.setOnClickListener {
            currentSession = "Open"
            binding.btnSessionOpen.setBackgroundResource(R.drawable.bg_session_tab_active)
            binding.btnSessionOpen.setTextColor(getColor(android.R.color.white))
            binding.btnSessionClose.setBackgroundResource(R.drawable.bg_session_tab_inactive)
            binding.btnSessionClose.setTextColor(getColor(R.color.text_secondary))
        }

        binding.btnSessionClose.setOnClickListener {
            currentSession = "Close"
            binding.btnSessionClose.setBackgroundResource(R.drawable.bg_session_tab_active)
            binding.btnSessionClose.setTextColor(getColor(android.R.color.white))
            binding.btnSessionOpen.setBackgroundResource(R.drawable.bg_session_tab_inactive)
            binding.btnSessionOpen.setTextColor(getColor(R.color.text_secondary))
        }
    }

    private fun setupGameOptionsGrid() {
        val gameOptions = listOf(
            GameOption("all_in_one", "All In One", R.drawable.ic_dice_all, false),
            GameOption("single_digit", "Single Digit", R.drawable.ic_dice_1, false),
            GameOption("single_digit_bulk", "Single Digit Bulk", R.drawable.ic_dice_1, true),
            GameOption("jodi_digit", "Jodi Digit", R.drawable.ic_dice_2, false),
            GameOption("jodi_digit_bulk", "Jodi Digit Bulk", R.drawable.ic_dice_2, true),
            GameOption("single_pana", "Single Pana", R.drawable.ic_card_single, false),
            GameOption("single_pana_bulk", "Single Pana Bulk", R.drawable.ic_card_single, true),
            GameOption("double_pana", "Double Pana", R.drawable.ic_card_double, false),
            GameOption("double_pana_bulk", "Double Pana Bulk", R.drawable.ic_card_double, true),
            GameOption("triple_pana", "Triple Pana", R.drawable.ic_card_triple, false),
            GameOption("triple_pana_bulk", "Triple Pana Bulk", R.drawable.ic_card_triple, true),
            GameOption("two_digit_cp_sr", "Two Digit(CP, SR)", R.drawable.ic_dice_2, false),
            GameOption("sp_motor", "Sp Motor", R.drawable.ic_gauge, false),
            GameOption("dp_motor", "Dp Motor", R.drawable.ic_gauge, false),
            GameOption("red_bracket", "Red Bracket", R.drawable.ic_brackets, false),
            GameOption("group_jodi", "Group Jodi", R.drawable.ic_users, false),
            GameOption("half_sangam_a", "Half Sangam A", R.drawable.ic_layout_grid, false),
            GameOption("half_sangam_b", "Half Sangam B", R.drawable.ic_layout_grid, false),
            GameOption("full_sangam", "Full Sangam", R.drawable.ic_boxes, false)
        )

        binding.rvGameOptions.layoutManager = GridLayoutManager(this, 2)
        binding.rvGameOptions.adapter = GameOptionAdapter(gameOptions) { option ->
            val intent = Intent(this, SubGameBetActivity::class.java).apply {
                putExtra("EXTRA_MARKET_NAME", marketName)
                putExtra("EXTRA_SESSION", currentSession)
                putExtra("EXTRA_GAME_ID", option.id)
                putExtra("EXTRA_GAME_TITLE", option.title)
                putExtra("EXTRA_IS_BULK", option.isBulk)
            }
            startActivity(intent)
        }
    }
}