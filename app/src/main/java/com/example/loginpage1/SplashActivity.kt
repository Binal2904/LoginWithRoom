package com.example.loginpage1

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage1.Constants.Companion.OTP_VERIFIED
import com.example.loginpage1.Constants.Companion.REGISTERED
import com.example.loginpage1.Constants.Companion.isUserRegistered
import com.example.loginpage1.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var sharedPrefUtils: SharedPrefUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        sharedPrefUtils = SharedPrefUtils(this)

        when (sharedPrefUtils!!.getInt(isUserRegistered)) {
            REGISTERED -> {
                navigateToOTPScreen()
            }

            OTP_VERIFIED -> {
                navigateToMainScreen()
            }

            else -> {
                navigateToRegistrationScreen()
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOTPScreen() {
        val intent = Intent(this, OTPActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegistrationScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}