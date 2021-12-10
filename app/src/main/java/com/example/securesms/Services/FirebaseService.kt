package com.example.securesms.Services

import android.util.Log
import com.example.securesms.utilities.Callback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot


class FirebaseService {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String?, password: String?, callback: Callback){
        if(!checkIfProvided(email, password)){
            return callback.onResult(false, "Data not provided", null)
        }

        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnSuccessListener { callback.onResult(true, null, null) }
            .addOnFailureListener{ error -> callback.onResult(false, error.message, null)}
    }

    fun register(userName: String, email: String?, password: String?, telNumber: String?, uniqueKey: String, callback: Callback){
        if(!checkIfProvided(userName, email, password, telNumber, uniqueKey)){
            return callback.onResult(false, "Data not provided", null)
        }

        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnSuccessListener { res ->
                db.runTransaction { transaction ->
                    val userPublicDataDoc = db.collection("users").document()
                    val userPublicData = hashMapOf(
                        "userId" to res.user!!.uid,
                        "phoneNumber" to telNumber,
                        "userName" to userName)

                    transaction.set(userPublicDataDoc, userPublicData)

                    val userPrivateDataDoc = db.collection("privateKeys").document()
                    val userPrivateData = hashMapOf(
                        "userId" to res.user!!.uid,
                        "key" to uniqueKey)

                    transaction.set(userPrivateDataDoc, userPrivateData)
                }
                .addOnSuccessListener { callback.onResult(true, null, null) }
                .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
            }
            .addOnFailureListener{ error -> callback.onResult(false, error.message, null) }
    }

    fun getCurrentUser(): FirebaseUser {
        return auth.currentUser!!;
    }

    fun getPrivateKey(callback: Callback){
        db.collection("privateKeys")
            .whereEqualTo("userId", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { res -> callback.onResult(true, null, res.elementAt(0).data["key"].toString()) }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    fun addContact(contactUserId: String, ownPublicKey: Int, contactPublicKey: Int, modulus: Int, base: Int, callback: Callback)
    {
        val contactData = hashMapOf(
            "UserIdA" to auth.currentUser!!.uid,
            "UserIdB" to contactUserId,
            "publicKeyA" to ownPublicKey,
            "publicKeyB" to contactPublicKey,
            "modulus" to modulus,
            "base" to base);

        db.collection("contacts")
            .document()
            .set(contactData)
            .addOnSuccessListener { callback.onResult(true, null, null) }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    private fun checkIfProvided(vararg parameters : String?) : Boolean{
        parameters.forEach { parameter ->
            if(parameter.isNullOrBlank()) return false;
        }
        return true;
    }
}