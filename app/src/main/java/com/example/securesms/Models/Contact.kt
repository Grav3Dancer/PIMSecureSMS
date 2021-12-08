package com.example.securesms.Models

import java.io.Serializable


data class Contact(
    val contactName:String,
    val publicKey:Int,
    val publicP:Int,
    val phoneNumber:String):Serializable;