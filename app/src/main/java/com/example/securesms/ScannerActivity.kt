package com.example.securesms

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val CAMERA_REQUEST_CODE = 101

class ScannerActivity : AppCompatActivity() {

    lateinit var buttonBack : FloatingActionButton

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        setupPermissions()
        initCodeScanner()

        buttonBack = findViewById(R.id.buttonBackToAddContact)
        buttonBack.setOnClickListener {
            this.finish()
        }
    }

    private fun initCodeScanner() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.apply{
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE // or CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        codeScanner.decodeCallback = DecodeCallback {
            // here we have it.text with decoded data
            setResult(Activity.RESULT_OK,
                Intent()
                    .putExtra("scannedText", it.text))
            finish()
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED ){
                    Toast.makeText(this, "You need camera permissions to use scanner",
                        Toast.LENGTH_LONG).show()
                }else{
                    // success, nice
                }
            }
        }
    }
}