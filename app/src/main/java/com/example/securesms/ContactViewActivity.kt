package com.example.securesms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.securesms.Services.EncryptionService

class ContactViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_view)


        val message = "";
        val privateKey = 12;
        val publicKey = 19;
        val publicP = 29435;
        val secretKey = EncryptionService.CalculateKey(publicP,publicKey,privateKey).toString();
        val decryptedMessage = EncryptionService.Decrypt(message,secretKey);
    }
}