package com.example.securesms

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.securesms.Adapters.MessagesListAdapter
import com.example.securesms.Models.Contact
import com.example.securesms.Models.ListSMS
import com.example.securesms.Models.SMS
import com.example.securesms.Services.EncryptionService
import com.google.android.material.textfield.TextInputLayout
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class ContactViewActivity : AppCompatActivity() {
    lateinit var messagesList : ListView
    lateinit var contactNameTextView : TextView
    lateinit var messageInput : TextInputLayout
    lateinit var contact : Contact
    var privateKey = 1
    var secretKey = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_view)

        contact = intent.getSerializableExtra("contact") as Contact
        val messages = (intent.getSerializableExtra("messages") as ListSMS).smses
        privateKey = intent.getIntExtra("privateKey", 1)
        Log.v("ContactView_privKey", privateKey.toString())

        secretKey = EncryptionService.CalculateKey(contact.publicP, contact.publicKey,privateKey.toBigInteger())

        messagesList = findViewById(R.id.messagesListView)
        contactNameTextView = findViewById(R.id.contactNameText)
        messageInput = findViewById(R.id.sendMessageInput)

        if (contact != null) {
            contactNameTextView.text = contact.contactName
        } else {
            contactNameTextView.text = "No contact name"
        }

        val adapter = MessagesListAdapter(this, secretKey.toString())
        messagesList.adapter = adapter
        messages.forEach {
            adapter.addItem(it)
        }

        messageInput.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_send_24)
        messageInput.isEndIconVisible = true
        messageInput.setEndIconOnClickListener {
            val text = messageInput.editText?.text.toString()
            if (text.isNotBlank()) {
                sendSMS(contact?.phoneNumber!!, text)
                messageInput.editText?.setText(" ")
                val date = Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000)
                adapter.addItem(SMS(date, contact.phoneNumber, text, true))
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow( window.decorView.rootView.windowToken,0)
                Toast.makeText(this, "Message sent", Toast.LENGTH_LONG).show()
            } else {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow( window.decorView.rootView.windowToken,0)
                Toast.makeText(this, "Cannot send empty message", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun sendSMS(phoneNumber: String, message: String) {
        val encryptedMessage = EncryptionService.Encrypt(message, secretKey.toString())
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, encryptedMessage, sentPI, null)
    }
}