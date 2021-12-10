package com.example.securesms.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.securesms.ContactViewActivity
import com.example.securesms.Models.Contact
import com.example.securesms.Models.ListSMS
import com.example.securesms.Models.SMS
import com.example.securesms.R

class ContactsListAdapter(val context:Context) : BaseAdapter(), Filterable {
    class ListObject(val contact:Contact,val messages: List<SMS>)

    var contactList = mutableListOf<ListObject>()
    var filteredContactList = mutableListOf<ListObject>();
    private val mFilter: ItemFilter = ItemFilter();

    override fun getCount(): Int {
        return filteredContactList.size
    }

    override fun getItem(p0: Int): Any {
        return filteredContactList[p0]
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
        filteredContactList = contactList;
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return mFilter;
    }


    inner class ItemFilter() : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterString = p0.toString().lowercase();
            var results:FilterResults = FilterResults();
            var data = contactList;
            var newData = data.filter { it.contact.contactName.lowercase().contains(filterString) || it.contact.phoneNumber.lowercase().contains(filterString) }
            results.values = newData;
            results.count = newData.count();
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredContactList = p1?.values as MutableList<ListObject>
            notifyDataSetChanged()
        }
    }
}
