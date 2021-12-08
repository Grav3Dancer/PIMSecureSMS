package com.example.securesms.Models

import java.util.*

data class LoggedUserData(
    val id: Int,
    val privateKey: Int,
    val login: String,
    val phoneNumber: String);