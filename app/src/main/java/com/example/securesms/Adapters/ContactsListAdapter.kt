package com.example.securesms.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.securesms.MainActivity
import com.example.securesms.R
import com.example.securesms.Services.SMS

class ContactsListAdapter(val context:Context) : BaseAdapter() {
    class ListObject(val name:String,val messages: List<SMS>);
    var contactList = mutableListOf<ListObject>();

    override fun getCount(): Int {
        return contactList.size;
    }

    override fun getItem(p0: Int): Any {
        return contactList[p0];
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var newContactView = LayoutInflater.from(context).inflate(R.layout.list_view_item, p2, false);
        val contact = getItem(p0) as ListObject;

        newContactView.findViewById<TextView>(R.id.ContactName).text = contact.name;
        newContactView.findViewById<TextView>(R.id.LastMessage).text = contact.messages.last().message;
        //newContactView.setOnClickListener { startActivity(Intent(context, MainActivity::class.java)) }
        return newContactView;
    }
    fun AddItem(name:String,messages: List<SMS>){
        contactList.add(ListObject(name, messages));
        notifyDataSetChanged();
    }
}