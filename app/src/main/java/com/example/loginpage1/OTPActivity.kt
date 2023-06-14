package com.example.loginpage1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginpage1.Constants.Companion.WHICH_SCREEN
import com.example.loginpage1.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private var sharedPrefUtils: SharedPrefUtils? = null
    private var isSignUpScreen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefUtils = SharedPrefUtils(this)
        isSignUpScreen = intent.getBooleanExtra(WHICH_SCREEN, false)
        binding.buttonRegisterOTP.setOnClickListener {
            if (isSignUpScreen) {
                Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT)
                    .show()
            }
            val intent = Intent(this@OTPActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}