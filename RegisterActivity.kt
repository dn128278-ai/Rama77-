package com.rama77.app.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rama77.app.MainActivity
import com.rama77.app.databinding.ActivityRegisterBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullscreenMode()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
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

    private fun setupListeners() {
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
        }

        binding.btnCompleteRegister.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        val mobile = binding.etMobileNumber.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
        val promo = binding.etPromoCode.text?.toString()?.trim().orEmpty()
        val newMpin = binding.etNewMpin.text?.toString()?.trim().orEmpty()
        val confirmMpin = binding.etConfirmMpin.text?.toString()?.trim().orEmpty()
        val biometricEnabled = binding.switchFingerprint.isChecked

        // 1. Validate Mobile Number
        if (mobile.length != 10) {
            binding.tilMobileNumber.error = "Please enter a valid 10-digit mobile number"
            binding.etMobileNumber.requestFocus()
            return
        } else {
            binding.tilMobileNumber.error = null
        }

        // 2. Validate Password
        if (password.length < 4) {
            binding.tilPassword.error = "Password must be at least 4 characters long"
            binding.etPassword.requestFocus()
            return
        } else {
            binding.tilPassword.error = null
        }

        // 3. Validate MPIN
        if (newMpin.length != 4) {
            binding.tilNewMpin.error = "Please enter a 4-digit MPIN"
            binding.etNewMpin.requestFocus()
            return
        } else {
            binding.tilNewMpin.error = null
        }

        if (newMpin != confirmMpin) {
            binding.tilConfirmMpin.error = "MPIN and Confirm MPIN do not match!"
            binding.etConfirmMpin.requestFocus()
            return
        } else {
            binding.tilConfirmMpin.error = null
        }

        // Show Loading Animation
        binding.btnCompleteRegister.text = ""
        binding.pbRegisterLoading.visibility = View.VISIBLE

        lifecycleScope.launch {
            delay(1200) // Simulating network registration API call

            // Persist user credentials in local secure preferences
            val prefs = getSharedPreferences("RAMA77_PREFS", Context.MODE_PRIVATE)
            prefs.edit().apply {
                putString("KEY_USER_PHONE", mobile)
                putString("KEY_USER_MPIN", newMpin)
                putBoolean("KEY_BIOMETRIC_ENABLED", biometricEnabled)
                putString("KEY_PROMO_CODE", promo)
                putInt("KEY_WALLET_BALANCE", if (promo.isNotEmpty()) 100 else 0)
                putBoolean("KEY_IS_LOGGED_IN", true)
                apply()
            }

            binding.pbRegisterLoading.visibility = View.GONE
            binding.btnCompleteRegister.text = "Complete Registration"

            Toast.makeText(
                this@RegisterActivity,
                "Registration Successful! Welcome to RAMA77.",
                Toast.LENGTH_LONG
            ).show()

            // Redirect directly to Home Dashboard (clearing back stack)
            val intent = Intent(this@RegisterActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("EXTRA_NEW_USER", true)
                putExtra("EXTRA_PHONE", mobile)
            }
            startActivity(intent)
            finish()
        }
    }
}