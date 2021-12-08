package com.example.securesms.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.securesms.ContactViewActivity
import com.example.securesms.Models.Contact
import com.example.securesms.Models.ListSMS
import com.example.securesms.Models.SMS
import com.example.securesms.R

class ContactsListAdapter(val context:Context) : BaseAdapter() {
    class ListObject(val contact:Contact,val messages: List<SMS>)
    var contactList = mutableListOf<ListObject>()

    override fun getCount(): Int {
        return contactList.size
    }

    override fun getItem(p0: Int): Any {
        return contactList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var newContactView = LayoutInflater.from(context).inflate(R.layout.list_view_item, p2, false)
        val item = getItem(p0) as ListObject

        newContactView.findViewById<TextView>(R.id.ContactName).text = item.contact.contactName
        newContactView.findViewById<TextView>(R.id.LastMessage).text = item.messages.lastOrNull()?.message ?: "";

        newContactView.setOnClickListener {
            val intent = Intent(context,ContactViewActivity::class.java);
            intent.putExtra("contact",item.contact);
            intent.putExtra("messages",ListSMS(item.messages));
            context.startActivity(intent);
        }
        return newContactView
    }
    fun AddItem(contact:Contact,messages: List<SMS>){
        contactList.add(ListObject(contact, messages))
        notifyDataSetChanged()
    }
}