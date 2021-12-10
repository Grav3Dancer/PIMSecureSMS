package com.example.securesms.Models

import java.math.BigInteger

data class ContactFirebaseModel(
    val IdUser1:String,
    val IdUser2:String,
    val publicKey:BigInteger,
    val publicKey2:BigInteger,
    val publicP:BigInteger,
    val publicG:BigInteger);