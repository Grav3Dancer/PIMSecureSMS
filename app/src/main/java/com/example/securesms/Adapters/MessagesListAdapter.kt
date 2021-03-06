package com.example.securesms.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.securesms.Models.SMS
import com.example.securesms.R
import com.example.securesms.Services.EncryptionService

class MessagesListAdapter(val context : Context, val key : String) : BaseAdapter() {
    var messages = mutableListOf<SMS>()

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val item = getItem(position) as SMS
        var newMessageView : View
        if (item.send) {
            newMessageView = LayoutInflater.from(context).inflate(R.layout.messages_list_item_sent, viewGroup, false)
            newMessageView.findViewById<TextView>(R.id.sentMessageText).text = EncryptionService.Decrypt(item.message, key)
        } else {
            newMessageView = LayoutInflater.from(context).inflate(R.layout.messages_list_item_received, viewGroup, false)
            newMessageView.findViewById<TextView>(R.id.receivedMessageText).text = EncryptionService.Decrypt(item.message, key)
        }

        return newMessageView
    }

    fun addItem(sms : SMS) {
        messages.add(sms)
        notifyDataSetChanged()
    }
}