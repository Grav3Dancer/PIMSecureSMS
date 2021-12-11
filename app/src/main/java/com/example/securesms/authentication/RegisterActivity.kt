package com.example.securesms.authentication

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
import com.example.securesms.R
import com.example.securesms.Services.FirebaseService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.util.jar.Manifest
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {

    lateinit var userNameInput : TextInputLayout
    lateinit var emailInput : TextInputLayout
    lateinit var passwordInput : TextInputLayout
    lateinit var passwordRepeatInput : TextInputLayout
    lateinit var phoneNumberInput : TextInputLayout
    lateinit var buttonRegister : Button
    lateinit var buttonBack : FloatingActionButton
    lateinit var telephonyManager : TelephonyManager

    private val database: FirebaseService = FirebaseService()
    private val permission = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userNameInput = findViewById(R.id.userNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        passwordRepeatInput = findViewById(R.id.passwordRepeatInput)
        phoneNumberInput = findViewById(R.id.phoneNumberInput)
        buttonRegister = findViewById(R.id.buttonCreateAccount)
        buttonBack = findViewById(R.id.buttonBack)
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), permission)
        }

        val number = telephonyManager.line1Number
        if(number.isNullOrBlank()){
            phoneNumberInput.isEnabled = true
        }
        else{
            phoneNumberInput.editText!!.setText(number)
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val username = userNameInput.editText!!.text.toString()
            val email = emailInput.editText!!.text.toString()
            val password = passwordInput.editText!!.text.toString()
            val passwordRepeat = passwordRepeatInput.editText!!.text.toString()
            val phoneHumber = phoneNumberInput.editText!!.text.toString()
            val uniqueKey = Random.nextInt(0, 20)

            if(password.equals(passwordRepeat)){
                database.register(username, email, password, phoneHumber, uniqueKey){ isSuccess, message, value ->
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