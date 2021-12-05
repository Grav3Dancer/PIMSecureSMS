package com.example.securesms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddContactActivity : AppCompatActivity() {
    lateinit var buttonBack : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        buttonBack = findViewById(R.id.buttonBackToMain)
        buttonBack.setOnClickListener {
            this.finish()
        }
    }
}