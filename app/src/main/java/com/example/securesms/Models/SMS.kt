package com.example.securesms.Models

import java.io.Serializable
import java.util.*

data class SMS(val date: Date, val phoneNumber: String, val message: String, val send: Boolean): Serializable;

data class ListSMS(val smses : List<SMS>):Serializable;