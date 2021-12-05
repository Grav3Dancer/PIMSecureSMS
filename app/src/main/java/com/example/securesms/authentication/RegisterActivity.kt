package com.example.securesms.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.securesms.MainActivity
import com.example.securesms.R
import com.example.securesms.Services.FirebaseService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    lateinit var emailInput : TextInputLayout
    lateinit var passwordInput : TextInputLayout
    lateinit var passwordRepeatInput : TextInputLayout
    lateinit var buttonRegister : Button
    lateinit var buttonBack : FloatingActionButton

    val database: FirebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        passwordRepeatInput = findViewById(R.id.passwordRepeatInput)
        buttonRegister = findViewById(R.id.buttonCreateAccount)
        buttonBack = findViewById(R.id.buttonBack)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val username = emailInput.editText!!.text.toString()
            val password = passwordInput.editText!!.text.toString()
            val passwordRepeat = passwordRepeatInput.editText!!.text.toString()

            if(password.equals(passwordRepeat)){
                database.register(username, password){ isSuccess, message ->
                    if (isSuccess) {
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        this.finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Provided passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            this.finish()
        }
    }
}