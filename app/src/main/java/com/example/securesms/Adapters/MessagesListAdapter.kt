package com.example.securesms.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.securesms.Models.SMS
import com.example.securesms.R

class MessagesListAdapter(val context : Context) : BaseAdapter() {
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
            newMessageView.findViewById<TextView>(R.id.sentMessageText).text = item.message
        } else {
            newMessageView = LayoutInflater.from(context).inflate(R.layout.messages_list_item_received, viewGroup, false)
            newMessageView.findViewById<TextView>(R.id.receivedMessageText).text = item.message
        }

        return newMessageView
    }

    fun addItem(sms : SMS) {
        messages.add(sms)
        notifyDataSetChanged()
    }
}