package com.example.securesms

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class ContactViewActivity : AppCompatActivity() {
    lateinit var messagesList : ListView
    lateinit var contactNameTextView : TextView
    lateinit var messageInput : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_view)

        val contact = intent.getSerializableExtra("contact") as Contact?
        val messages = (intent.getSerializableExtra("messages") as ListSMS).smses

        messagesList = findViewById(R.id.messagesListView)
        contactNameTextView = findViewById(R.id.contactNameText)
        messageInput = findViewById(R.id.sendMessageInput)

        if (contact != null) {
            contactNameTextView.text = contact.contactName
        } else {
            contactNameTextView.text = "No contact name"
        }

        val adapter = MessagesListAdapter(this)
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

        val message = "";
        val privateKey = 12;
        val publicKey = 19;
        val publicP = 29435;
        val secretKey = EncryptionService.CalculateKey(publicP,publicKey,privateKey).toString();
        val decryptedMessage = EncryptionService.Decrypt(message,secretKey);
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
    }
}