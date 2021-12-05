package com.example.securesms

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.securesms.authentication.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class AddContactActivity : AppCompatActivity() {
    lateinit var buttonBack : FloatingActionButton
    lateinit var buttonGoToScanner : Button

    private lateinit var imageViewQR: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        buttonBack = findViewById(R.id.buttonBackToMain)
        buttonBack.setOnClickListener {
            this.finish()
        }

        buttonGoToScanner = findViewById(R.id.buttonGoToScanner)
        buttonGoToScanner.setOnClickListener {
            val intent  = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

        generateQrCode()
    }

    private fun generateQrCode() {

        imageViewQR = findViewById(R.id.imageViewQR)
        val data = "fuck android studio" // here should be data do encode

        if(data.isEmpty()){
            Toast.makeText(this, "No data to generate QR Code",
                Toast.LENGTH_LONG).show()
        }
        else {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
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
}