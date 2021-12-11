package com.example.securesms.DataModels

import java.io.Serializable
import java.math.BigInteger

data class ContactDataModel(
    val userIdA: String = "",
    val userIdB: String = "",
    val publicKeyA: Int = -1,
    val publicKeyB: Int = -1,
    val modulus: Int = -1,
    val base: Int = -1) : Serializable