package com.example.securesms.Services

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.securesms.Models.Contact
import com.example.securesms.Models.SMS
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors


class SmsService(val context: Context){

    fun GetMessages(contacts: List<Contact>):Map<Contact, List<SMS>>{
        val groupedMessages = GroupByPhoneNumber(GetAllSMS())
        return LinkSMSesToContacts(groupedMessages,contacts)
    }

    private fun TimeStampToDate(timestamp:String):Date{
        return Date(timestamp.toLong())
    }

    private fun GetAllSMS():MutableList<SMS>{
        val smsList: MutableList<SMS> = ArrayList()
        GetReceivedSMS(smsList)
        GetSendedSMS(smsList)
        smsList.sortWith(compareBy({it.date}))
        return smsList
        }

    private fun GetSendedSMS(smsList: MutableList<SMS>) {
        val cursorSent =
            context.contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null)
        if (cursorSent!!.moveToFirst()) {
            do {
                var phoneNumber =  cursorSent.getString(2);
                smsList.add(
                    SMS(
                        TimeStampToDate(cursorSent.getString(4)),
                        if (phoneNumber.length > 9) phoneNumber.substring(phoneNumber.length - 9) else phoneNumber,
                        cursorSent.getString(12),
                        true
                    )
                )
            } while (cursorSent.moveToNext())
        }
    }

    private fun GetReceivedSMS(smsList: MutableList<SMS>) {
        val cursorInbox =
            context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        if (cursorInbox!!.moveToFirst()) {
            do {
                var phoneNumber =  cursorInbox.getString(2);
                smsList.add(
                    SMS(
                        TimeStampToDate(cursorInbox.getString(4)),
                        if (phoneNumber.length > 9) phoneNumber.substring(phoneNumber.length - 9) else phoneNumber,
                        cursorInbox.getString(12),
                        false
                    )
                )
            } while (cursorInbox.moveToNext())
        }
    }

    private fun GroupByPhoneNumber(list: MutableList<SMS>):Map<String, List<SMS>>{
        return list.stream().collect(Collectors.groupingBy { w -> w.phoneNumber })
    }

    private fun LinkSMSesToContacts(SMS:Map<String, List<SMS>>,contacts:List<Contact>):Map<Contact, List<SMS>> {
        var ContactSMSMap : MutableMap<Contact,List<SMS>> = mutableMapOf();
        for (contact in contacts) {
            val sms = SMS.get(contact.phoneNumber) ?: mutableListOf();
            ContactSMSMap.put(contact,sms)
        }
        return ContactSMSMap;
    }
}