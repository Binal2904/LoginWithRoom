package com.example.loginpage1

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loginpage1.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var binding: ActivityRegisterBinding
    private var sharedPrefUtils: SharedPrefUtils? = null
    private lateinit var selectedImageUri: Uri
    var imageUrl: String = ""

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
        private const val REQUEST_IMAGE_PICK = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefUtils = SharedPrefUtils(this)
        databaseHelper = DatabaseHelper(this)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            buttonRegister.setOnClickListener {
                val username = editTextUsername.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                register(username, password, imageUrl)
            }

            textViewRegister.setOnClickListener {
                navigateToLoginScreen()
            }

            if (hasRequiredPermissions()) {
                enableImagePicker()
            } else {
                requestPermissions()
            }

        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun register(username: String, password: String, imageUrl: String) {
        if (validateInput(username, password, imageUrl)) {
            databaseHelper.registerUser(username, password, imageUrl)
            Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
            sharedPrefUtils!!.setStr(Constants.USER_NAME, username)
            intent.putExtra(Constants.WHICH_SCREEN, true)
            startActivity(intent)
            finish()
        }

    }

    private fun validateInput(username: String, password: String, imageUrl: String): Boolean {
        if (username.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_username), Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_password), Toast.LENGTH_SHORT)
                .show()
            return false

        } else if (imageUrl.isEmpty()) {
            Toast.makeText(
                this, getString(R.string.please_select_profile_picture), Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }

    private fun hasRequiredPermissions(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return readPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE
        )
    }

    private fun enableImagePicker() {
        binding.ivProfilePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (hasRequiredPermissions()) {
                enableImagePicker()
            } else {
                Toast.makeText(
                    this, getString(R.string.please_allow_permission_first), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun processAndSaveImage(imageUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val cacheDir = cacheDir
        val imageFile = File(cacheDir, System.currentTimeMillis().toString() + ".jpg")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        imageUrl = imageFile.toURI().toString()
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val uri: Uri? = data?.data
                uri?.let {
                    selectedImageUri = it
                    lifecycleScope.launch(Dispatchers.IO) {
                        processAndSaveImage(it)
                    }
                    Glide.with(applicationContext).load(selectedImageUri)
                        .into(binding.ivProfilePhoto)
                }
            }
        }

}