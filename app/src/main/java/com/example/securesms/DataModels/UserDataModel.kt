package com.example.securesms.DataModels

import java.io.Serializable

data class UserDataModel(
    val userId: String = "",
    val phoneNumber: String = "",
    val userName: String = "") : Serializable