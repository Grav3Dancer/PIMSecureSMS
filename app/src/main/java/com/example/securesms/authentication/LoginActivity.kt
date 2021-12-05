package com.example.securesms.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.securesms.MainActivity
import com.example.securesms.R
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    lateinit var usernameInput : TextInputLayout
    lateinit var passwordInput : TextInputLayout
    lateinit var buttonLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("login", "HUBERT")
            intent.putExtra("cosinnego", "DUPA")
            startActivity(intent)
            this.finish()
        }
    }
}