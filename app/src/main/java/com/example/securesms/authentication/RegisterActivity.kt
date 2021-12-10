package com.example.securesms.authentication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.securesms.MainActivity
import com.example.securesms.R
import com.example.securesms.Services.FirebaseService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var emailInput : TextInputLayout
    lateinit var passwordInput : TextInputLayout
    lateinit var passwordRepeatInput : TextInputLayout
    lateinit var buttonRegister : Button
    lateinit var telNumber : String
    lateinit var buttonBack : FloatingActionButton
    lateinit var tm : TelephonyManager

    val database: FirebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        passwordRepeatInput = findViewById(R.id.passwordRepeatInput)
        buttonRegister = findViewById(R.id.buttonCreateAccount)
        buttonBack = findViewById(R.id.buttonBack)
        tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

/*        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
                telNumber = tm.line1Number
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2)
            }
        }*/
        telNumber = "13123123"

        buttonRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val username = emailInput.editText!!.text.toString()
            val password = passwordInput.editText!!.text.toString()
            val passwordRepeat = passwordRepeatInput.editText!!.text.toString()
            val uniqueKey = java.util.UUID.randomUUID().toString().take(16)

            if(password.equals(passwordRepeat)){
                database.register(username, password, telNumber, uniqueKey){ isSuccess, message ->
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