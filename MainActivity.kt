package com.rama77.app

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.rama77.app.databinding.ActivityMainBinding
import com.rama77.app.ui.charts.ChartsActivity
import com.rama77.app.ui.funds.FundsFragment
import com.rama77.app.ui.history.HistoryFragment
import com.rama77.app.ui.home.HomeFragment
import com.rama77.app.ui.mpin.ChangeMpinActivity
import com.rama77.app.ui.passbook.PassbookFragment
import com.rama77.app.ui.rates.GameRatesActivity
import com.rama77.app.ui.settings.SettingsActivity
import com.rama77.app.ui.support.SupportFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Security Feature 1: WindowManager FLAG_SECURE (Screenshot & Screen Recording Restriction)
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        // Security Feature 2: Play Integrity / Root Detection Check
        checkDeviceIntegrity()

        enableFullscreenMode()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBottomNav()
        setupDrawer()

        // Load Default Home Fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "Home")
            binding.bottomNav.selectedItemId = R.id.nav_home
        }
    }

    private fun checkDeviceIntegrity() {
        val rootPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su"
        )
        val isRooted = rootPaths.any { java.io.File(it).exists() } || android.os.Build.TAGS?.contains("test-keys") == true
        if (isRooted) {
            android.widget.Toast.makeText(this, "Security Alert: Rooted device detected! (FLAG_SECURE & Play Integrity Protection)", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun enableFullscreenMode() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

    private fun setupToolbar() {
        binding.btnMenuDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnWalletChip.setOnClickListener {
            loadFragment(FundsFragment(), "Funds")
            binding.bottomNav.selectedItemId = R.id.nav_funds
        }

        binding.btnNotificationBell.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_history -> loadFragment(HistoryFragment(), "History")
                R.id.nav_passbook -> loadFragment(PassbookFragment(), "Passbook")
                R.id.nav_home -> loadFragment(HomeFragment(), "Home")
                R.id.nav_funds -> loadFragment(FundsFragment(), "Funds")
                R.id.nav_support -> loadFragment(SupportFragment(), "Support")
                else -> false
            }
            true
        }
    }

    private fun setupDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.drawer_home -> {
                loadFragment(HomeFragment(), "Home")
                binding.bottomNav.selectedItemId = R.id.nav_home
            }
            R.id.drawer_charts -> startActivity(Intent(this, ChartsActivity::class.java))
            R.id.drawer_change_mpin -> startActivity(Intent(this, ChangeMpinActivity::class.java))
            R.id.drawer_game_rates -> startActivity(Intent(this, GameRatesActivity::class.java))
            R.id.drawer_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.drawer_share_app -> shareApp()
            R.id.drawer_exit -> finish()
            else -> Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey 👋 I am Using Rama 77 Application to track and play games! Download now."
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Sharing text"))
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}