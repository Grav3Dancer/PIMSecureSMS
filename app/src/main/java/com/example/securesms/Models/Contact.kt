package com.example.securesms.Models

import java.io.Serializable
import java.math.BigInteger


data class Contact(
    val contactName:String,
    val publicKey:BigInteger,
    val publicP:BigInteger,
    val phoneNumber:String):Serializable;