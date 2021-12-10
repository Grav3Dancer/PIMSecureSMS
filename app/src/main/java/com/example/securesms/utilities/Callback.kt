package com.example.securesms.utilities

fun interface Callback {
    fun onResult(isSuccess: Boolean, message: String?, value: Any?)
}