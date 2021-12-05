package com.example.securesms.Services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FirebaseService {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!;
    }

    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { user ->
                Log.v("LOGIN", user.user!!.uid)
            }
            .addOnFailureListener{ error -> Log.v("LOGIN", error.message.toString())}
    }

    fun register(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { Log.v("LOGIN", "rejestracja sie powiodla :D") }
            .addOnFailureListener{ error -> Log.v("LOGIN", error.message.toString()) }
    }
}