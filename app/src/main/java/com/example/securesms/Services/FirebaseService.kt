package com.example.securesms.Services

import android.util.Log
import com.example.securesms.utilities.Callback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FirebaseService {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!;
    }

    fun login(email: String?, password: String?, callback: Callback){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            return callback.onResult(false, "Data not provided")
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback.onResult(true, null) }
            .addOnFailureListener{ error -> callback.onResult(false, error.message)}
    }

    fun register(email: String?, password: String?, callback: Callback){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            return callback.onResult(false, "Data not provided")
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback.onResult(true, null) }
            .addOnFailureListener{ error -> callback.onResult(false, error.message) }
    }
}