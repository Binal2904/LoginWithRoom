package com.example.loginpage1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.loginpage1.Constants.Companion.WHICH_SCREEN
import com.example.loginpage1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var binding: ActivityLoginBinding
    private var sharedPrefUtils: SharedPrefUtils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)
        sharedPrefUtils = SharedPrefUtils(this)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            buttonLogin.setOnClickListener {
                val username = editTextUsername.text.toString().trim()
                val password = editTextPassword.text.toString().trim()

                login(username, password)
            }
            textViewRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login(username: String, password: String) {
        if (validateInput(username, password)) {
            val user = databaseHelper.loginUser(username, password)
            if (user) {
                sharedPrefUtils!!.setStr(Constants.USER_NAME, username)
                navigateToOTPScreen()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_username_or_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.invalid_username_or_password),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToOTPScreen() {
        val intent = Intent(this, OTPActivity::class.java)
        intent.putExtra(WHICH_SCREEN, false)
        startActivity(intent)
        finish()
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.please_fill_in_all_the_fields),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}