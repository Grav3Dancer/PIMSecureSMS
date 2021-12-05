package com.example.securesms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.securesms.Adapters.ContactsListAdapter
import com.example.securesms.Services.SmsService

class MainActivity : AppCompatActivity() {
    lateinit var contactListView: ListView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.ContactsListView);
        val smsService= SmsService(this.applicationContext);
        val x = mutableListOf<String>("Microsoft", "SAN_PL");
        val groupedMessages = smsService.GetMessages(x);

        val adapter = ContactsListAdapter(this);
        contactListView.adapter = adapter;
        groupedMessages.forEach { s, list ->  adapter.AddItem(s,list)};
    }
}