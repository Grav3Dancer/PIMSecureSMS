package com.example.securesms.Models

import java.math.BigInteger

data class ContactQRData(
    val contactId: String,
    val publicKey:BigInteger,
    val publicP:BigInteger,
    val publicG:BigInteger)