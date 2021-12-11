package com.example.securesms.authentication

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.securesms.MainActivity
import com.example.securesms.R
import com.example.securesms.Services.FirebaseService
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    lateinit var emailInput : TextInputLayout
    lateinit var passwordInput : TextInputLayout
    lateinit var buttonLogin : Button
    lateinit var buttonRegister : Button

    val database: FirebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                }
            }

        requestPermissionLauncher.launch(
            Manifest.permission.READ_SMS)

        buttonLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val username = emailInput.editText!!.text.toString()
            val password = passwordInput.editText!!.text.toString()
            database.login(username, password){ isSuccess, message, value ->
                if (isSuccess) {
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    this.finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}