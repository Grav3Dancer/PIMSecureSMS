package com.example.securesms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.securesms.Adapters.ContactsListAdapter
import com.example.securesms.Services.SmsService
import android.widget.Button
import com.example.securesms.Models.Contact
import com.example.securesms.authentication.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var buttonAddContact : Button
    lateinit var contactListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.ContactsListView)
        val smsService= SmsService(this.applicationContext)
        val x = mutableListOf<Contact>(Contact("x",123,67890,"123456789"))
        val groupedMessages = smsService.GetMessages(x)

        val adapter = ContactsListAdapter(this)
        contactListView.adapter = adapter
        groupedMessages.forEach { (s, list) ->  adapter.AddItem(s,list)}
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