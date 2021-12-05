package com.example.securesms.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.securesms.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    lateinit var passwordInput : TextInputLayout
    lateinit var passwordRepeatInput : TextInputLayout
    lateinit var buttonRegister : Button
    lateinit var buttonBack : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        passwordInput = findViewById(R.id.passwordInput)
        passwordRepeatInput = findViewById(R.id.passwordRepeatInput)
        buttonRegister = findViewById(R.id.buttonCreateAccount)
        buttonBack = findViewById(R.id.buttonBack)

        buttonRegister.setOnClickListener {
            Log.v("secureSMS_registerActivity", "Tried to register xD")
        }

        buttonBack.setOnClickListener {
            this.finish()
        }
    }
}