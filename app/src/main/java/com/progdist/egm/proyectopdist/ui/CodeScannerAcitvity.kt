package com.progdist.egm.proyectopdist.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.progdist.egm.proyectopdist.databinding.ActivityCodeScannerBinding


class CodeScannerAcitvity : AppCompatActivity() {

    private lateinit var _binding: ActivityCodeScannerBinding
    val binding get() = _binding

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCodeScannerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val scannerView = binding.scannerView
        codeScanner = CodeScanner(this,scannerView)

        binding.btnSaveCode.visibility = View.GONE

        codeScanner.startPreview()
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ONE_DIMENSIONAL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        if(binding.switchAutomatic.isChecked.not()){
            binding.root.showToast("Pulsa sobre la vista de la cámara para volver a escanear")
        }


        binding.switchAutomatic.setOnCheckedChangeListener { compoundButton, b ->
            if(binding.switchAutomatic.isChecked){
                codeScanner.startPreview()
                codeScanner.scanMode = ScanMode.CONTINUOUS
            } else {
                codeScanner.scanMode = ScanMode.SINGLE
                binding.root.showToast("Pulsa sobre la vista de la cámara para volver a escanear")
            }
        }

        val DELAY = 1000
        var lastTimestamp: Long = 0

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback { scanResult->

            if(System.currentTimeMillis() - lastTimestamp < DELAY){
                return@DecodeCallback
            }

            runOnUiThread {
                binding.btnSaveCode.visibility = View.VISIBLE

                binding.tvScanStatus.setTextAnimation("Resultado: ${scanResult.text}",200)

                binding.btnSaveCode.setOnClickListener {
                    val data = Intent()
                    data.putExtra("code", scanResult.text)
                    setResult(RESULT_OK, data)
                    finish()
                }

            }
            lastTimestamp = System.currentTimeMillis()
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
            binding.btnSaveCode.visibility = View.GONE
            binding.tvScanStatus.text = "Escaneando..."
        }

    }

    override fun onPause() {
        super.onPause()
        codeScanner.stopPreview()
        codeScanner.releaseResources()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
}