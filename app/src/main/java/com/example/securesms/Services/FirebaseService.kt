package com.example.securesms.Services

import android.util.Log
import com.example.securesms.DataModels.ContactDataModel
import com.example.securesms.DataModels.UserDataModel
import com.example.securesms.Models.Contact
import com.example.securesms.utilities.Callback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FirebaseService {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun login(email: String?, password: String?, callback: Callback){
        if(!checkIfProvided(email, password)){
            return callback.onResult(false, "Data not provided", null)
        }

        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnSuccessListener { callback.onResult(true, null, null) }
            .addOnFailureListener{ error -> callback.onResult(false, error.message, null)}
    }

    fun register(userName: String, email: String?, password: String?, telNumber: String?, uniqueKey: Int, callback: Callback){
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
            .addOnSuccessListener { res -> callback.onResult(true, null, res.elementAt(0).data["key"]) }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    fun addContact(contactUserId: String, ownPublicKey: Int, contactPublicKey: Int, modulus: Int, base: Int, callback: Callback)
    {
        val contactData = hashMapOf(
            "userIdA" to auth.currentUser!!.uid,
            "userIdB" to contactUserId,
            "publicKeyA" to ownPublicKey,
            "publicKeyB" to contactPublicKey,
            "modulus" to modulus,
            "base" to base)

        db.collection("contacts")
            .document()
            .set(contactData)
            .addOnSuccessListener { callback.onResult(true, null, null) }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    fun getContacts(callback: Callback)
    {
        val contacts = mutableListOf<Contact>()

        getContactList(){ isSuccess, message, value ->
            if(isSuccess == false){
                callback.onResult(false, message, value)
            }

            val contactDataModels = value as List<ContactDataModel>

            for(contactDataModel in contactDataModels) {
                var contact: Contact

                val isContactUserA: Boolean = auth.currentUser!!.uid == contactDataModel.userIdB //TODO auth.currentUser!!.uid

                val contactUserId =
                    if(isContactUserA) contactDataModel.userIdA
                    else contactDataModel.userIdB

                getContactUser(contactUserId) { innerIsSuccess, innerMessage, innerValue ->
                    if(innerIsSuccess == false){
                        callback.onResult(false, innerMessage, innerValue)
                    }
                    val user = innerValue as UserDataModel

                    contact = if(isContactUserA){
                        Contact(
                            user.userName,
                            contactDataModel.publicKeyA.toBigInteger(),
                            contactDataModel.modulus.toBigInteger(),
                            user.phoneNumber)
                    } else{
                        Contact(
                            user.userName,
                            contactDataModel.publicKeyB.toBigInteger(),
                            contactDataModel.modulus.toBigInteger(),
                            user.phoneNumber)
                    }

                    contacts.add(contact)

                    if(contactDataModel == contactDataModels.last()){
                        callback.onResult(true, null, contacts)
                    }
                }
            }
        }
    }

    private fun getContactUser(userId: String, callback: Callback)
    {
        db.collection("users")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { res -> callback.onResult(true, null, res.first().toObject(UserDataModel::class.java)) }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    private fun getContactList(callback: Callback)
    {
        val contacts = mutableListOf<ContactDataModel>()

        db.collection("contacts")
            .whereEqualTo("userIdA", auth.currentUser!!.uid) //TODO auth.currentUser!!.uid
            .get()
            .addOnSuccessListener { res ->
                val firstIterationContacts = res.toObjects(ContactDataModel::class.java)

                contacts.addAll(firstIterationContacts)

                db.collection("contacts")
                    .whereEqualTo("userIdB", auth.currentUser!!.uid) //TODO auth.currentUser!!.uid
                    .get()
                    .addOnSuccessListener { innerRes ->
                        val secondIterationContacts = innerRes.toObjects(ContactDataModel::class.java)

                        contacts.addAll(secondIterationContacts)

                        callback.onResult(true, null, contacts)
                    }
                    .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
            }
            .addOnFailureListener { error -> callback.onResult(false, error.message, null) }
    }

    private fun checkIfProvided(vararg parameters : Any?) : Boolean{
        parameters.forEach { parameter ->
            if(parameter is String? && parameter.isNullOrBlank()) return false
            if(parameter is Int? && parameter == null) return false;
        }
        return true
    }
}