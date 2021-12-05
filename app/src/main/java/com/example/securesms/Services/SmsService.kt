package com.example.securesms.Services

import android.content.Context
import android.net.Uri
import android.util.Log
import java.util.*
import java.util.stream.Collectors

data class SMS(val date: Date, val phoneNumber: String, val message: String, val send: Boolean)

class SmsService(val context: Context){

    fun GetMessages(phoneList:MutableList<String>):Map<String, List<SMS>>{
        val groupedMessages = GroupByPhoneNumber(GetAllSMS())
        return RemoveSMSNotFromList(groupedMessages,phoneList)
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
                var msgData = ""
                for (idx in 0 until cursorSent.getColumnCount()) {
                    msgData += " " + cursorSent.getColumnName(idx)
                        .toString() + ":" + cursorSent.getString(idx)
                }
                Log.v("s", msgData)
                smsList.add(
                    SMS(
                        TimeStampToDate(cursorSent.getString(4)),
                        cursorSent.getString(2),
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
                smsList.add(
                    SMS(
                        TimeStampToDate(cursorInbox.getString(4)),
                        cursorInbox.getString(2),
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
    private fun RemoveSMSNotFromList(SMS:Map<String, List<SMS>>,phoneNumbers:List<String>):Map<String, List<SMS>> {
        return SMS.filter { (key,value) -> phoneNumbers.contains(key)}
    }
}