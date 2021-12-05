package com.example.securesms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.securesms.authentication.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var buttonAddContact : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonAddContact = findViewById(R.id.buttonAddContact)
        buttonAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        val intent  = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        super.onDestroy()
    }
}