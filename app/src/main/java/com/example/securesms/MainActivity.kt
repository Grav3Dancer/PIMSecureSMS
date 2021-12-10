package com.example.securesms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ListView
import com.example.securesms.Adapters.ContactsListAdapter
import com.example.securesms.Services.SmsService
import android.widget.Button
import com.example.securesms.Models.Contact
import com.example.securesms.Models.SMS
import com.example.securesms.authentication.LoginActivity
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    lateinit var buttonAddContact : Button
    lateinit var contactListView: ListView
    lateinit var searchBar : TextInputLayout
    lateinit var contacts : MutableList<Contact>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contacts = GetContacts();
        searchBar = findViewById(R.id.searchBarLayout);
        contactListView = findViewById(R.id.ContactsListView)
        ListSMSes(GetListOfSMSes(contacts));

        searchBar.editText?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return;
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                SortContacts(p0.toString());
            }

            override fun afterTextChanged(p0: Editable?) {
                return;
            }

        })

    }
    private fun SortContacts(contactFilter: String){
        (contactListView.adapter as ContactsListAdapter).filter.filter(contactFilter);
    }
    private fun GetContacts(): MutableList<Contact> {
        //call api
        return mutableListOf<Contact>(Contact("Microsoft", 123, 67890, "Microsoft"),
            Contact("SAN_PL",213,34355,"SAN_PL")
        );
    }

    private fun GetListOfSMSes(contacts : MutableList<Contact>):Map<Contact,List<SMS>>{
        val smsService = SmsService(this.applicationContext)
        return smsService.GetMessages(contacts)
    }

    private fun ListSMSes(messages:Map<Contact,List<SMS>>) {
        val adapter = ContactsListAdapter(this)
        contactListView.adapter = adapter
        messages.forEach { (s, list) -> adapter.AddItem(s, list) }
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