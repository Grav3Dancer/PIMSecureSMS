package com.example.securesms

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.securesms.Models.ContactFirebaseModel
import com.example.securesms.Models.ContactQRData
import com.example.securesms.Services.EncryptionService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.math.BigInteger
import java.util.*

class AddContactActivity : AppCompatActivity() {
    lateinit var buttonBack : FloatingActionButton
    lateinit var buttonGoToScanner : Button
    lateinit var scannedText : TextView
    lateinit var userId : String
    var privateKey = 0

    private lateinit var imageViewQR: ImageView

    val prefix = "q1R3cO3dE7"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        userId = intent.getStringExtra("userId")!!
        privateKey = intent.getIntExtra("privateKey", 0)

        scannedText = findViewById(R.id.scannedText)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result : ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanned = result.data!!.getStringExtra("scannedText")
                scannedText.text = scanned
                addNewContact(scanned!!)
            }
        }

        buttonBack = findViewById(R.id.buttonBackToMain)
        buttonBack.setOnClickListener {
            this.finish()
        }

        buttonGoToScanner = findViewById(R.id.buttonGoToScanner)
        buttonGoToScanner.setOnClickListener {
            val intent  = Intent(this, ScannerActivity::class.java)
            startForResult.launch(intent)
        }

        val p = BigInteger(16, 100, Random())
        val g = BigInteger(5, 100, Random())
        val publicKey = EncryptionService.CalculateKey(p, g, privateKey.toBigInteger())

        val data = ContactQRData(userId!!, publicKey, p, g)

        generateQrCode(data)
    }

    private fun generateQrCode(data : ContactQRData) {

        imageViewQR = findViewById(R.id.imageViewQR)
        val dataString = "$prefix:${data.contactId}:${data.publicG}:${data.publicKey}:${data.publicP}"
        Log.v("qrdatatest", dataString)
        if(dataString.isEmpty()){
            Toast.makeText(this, "No data to generate QR Code",
                Toast.LENGTH_LONG).show()
        }
        else {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(dataString, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for(x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                imageViewQR.setImageBitmap(bmp)
            }catch (e: WriterException){
                e.printStackTrace()
            }
        }
    }

    //val data = ContactQRData(userId!!, publicKey, p, g)

    private fun addNewContact(scannedText : String) {
        val scannedTextFields = scannedText.split(":")
        if (scannedTextFields[0] == prefix && scannedTextFields.size == 5) {
            val data = ContactQRData(scannedTextFields[1],
                scannedTextFields[2].toBigInteger(),
                scannedTextFields[3].toBigInteger(),
                scannedTextFields[4].toBigInteger())

            val fbContact = ContactFirebaseModel(
                userId,
                data.contactId,
                EncryptionService.CalculateKey(data.publicP, data.publicG, privateKey.toBigInteger()),
                data.publicKey,
                data.publicP,
                data.publicG)

            //TODO send to firebase
        } else {
            Toast.makeText(this, "Wrong QR code data", Toast.LENGTH_LONG).show()
        }
    }
}