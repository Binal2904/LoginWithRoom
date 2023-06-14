package com.example.loginpage1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginpage1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    lateinit var binding: ActivityMainBinding
    private var listUser: User = User()
    private var sharedPrefUtils: SharedPrefUtils? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        sharedPrefUtils = SharedPrefUtils(this)
        initViews()

    }

    private fun initViews() {
        val username = sharedPrefUtils!!.getStr(Constants.USER_NAME)
        listUser = username?.let { databaseHelper.getUserData(it) }!!

        if (listUser != null) {
            binding.editTextUsername.text = listUser.username
            Glide.with(applicationContext).load(listUser.ProfilePhoto).into(binding.ivProfilePhoto)
        }
        binding.apply {
            username.let { databaseHelper.getUserData(it) }
            buttonDelete.setOnClickListener {
                val userDeleted = databaseHelper.deleteUser(listUser.username)
                if (userDeleted) {
                    listUser = User()
                    binding.editTextUsername.text = listUser.username
                    Toast.makeText(
                        this@MainActivity, getString(R.string.user_deleted), Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
                } else {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.user_not_deleted), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            buttonLogout.setOnClickListener {
                sharedPrefUtils!!.clearPref()
                Toast.makeText(
                    this@MainActivity, getString(R.string.user_loggedout), Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

}