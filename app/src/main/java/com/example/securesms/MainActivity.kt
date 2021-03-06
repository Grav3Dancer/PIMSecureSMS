package com.example.securesms

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ListView
import com.example.securesms.Adapters.ContactsListAdapter
import com.example.securesms.Services.SmsService
import android.widget.Button
import android.widget.ImageButton
import com.example.securesms.Models.Contact
import com.example.securesms.Models.SMS
import com.example.securesms.Services.FirebaseService
import com.example.securesms.authentication.LoginActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigInteger
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var buttonAddContact : Button
    lateinit var contactListView: ListView
    lateinit var searchBar : TextInputLayout
    lateinit var contacts : MutableList<Contact>;
    lateinit var adapter : ContactsListAdapter
    lateinit var refreshButton : ImageButton
    lateinit var firebaseService: FirebaseService
    lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contacts = GetContacts();
        searchBar = findViewById(R.id.searchBarLayout);
        contactListView = findViewById(R.id.ContactsListView)
        refreshButton = findViewById(R.id.refresh)
        adapter = ContactsListAdapter(this)
        contactListView.adapter = adapter

        firebaseService = FirebaseService()
        uid = firebaseService.getCurrentUser().uid

        ListSMSes(GetListOfSMSes(contacts))

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

        refreshButton.setOnClickListener {
            ListSMSes(GetListOfSMSes(contacts))
        }

    }
    private fun SortContacts(contactFilter: String){
        (contactListView.adapter as ContactsListAdapter).filter.filter(contactFilter);
    }
    private fun GetContacts(): MutableList<Contact> {
        //call api
        val xd = 213
        val xd2 = 34355
        val pkey = xd.toBigInteger()
        val pubP = xd2.toBigInteger()
        return mutableListOf<Contact>(Contact("Microsoft", pkey, pubP, "Microsoft"),
            Contact("SAN_PL",pkey,pubP,"SAN_PL"),
            Contact("TestowyKontaktXD",pkey,pubP,"123456780")
        );
    }

    private fun GetListOfSMSes(contacts : MutableList<Contact>):Map<Contact,List<SMS>>{
        val smsService = SmsService(this.applicationContext)
        return smsService.GetMessages(contacts)
    }

    private fun ListSMSes(messages:Map<Contact,List<SMS>>) {
        adapter.clearItems()
        messages.forEach { (s, list) -> adapter.AddItem(s, list) }
        buttonAddContact = findViewById(R.id.buttonAddContact)
        buttonAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            intent.putExtra("userId", uid) //TODO nasze uid
            intent.putExtra("privateKey", 10) //TODO legit private key
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        val intent  = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        super.onDestroy()
    }
}