package com.example.app_datn_haven_inn.ui.cccd

import android.Manifest
import android.provider.Settings
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.barcode.BarcodeScanning
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureFrontActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var capturedImageView: ImageView
    private lateinit var captureButton: ImageView
    private lateinit var img_back_mt: ImageView
    private lateinit var progressBar: ProgressBar

    private var imageCapture: ImageCapture? = null
    private var outputFile: File? = null
    private lateinit var cameraExecutor: ExecutorService

    private var extractedInfo: MutableMap<String, String> = mutableMapOf()

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
        private const val TAG = "CaptureFrontActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_front)

        // Khởi tạo các view
        previewView = findViewById(R.id.previewView)
        capturedImageView = findViewById(R.id.capturedImage)
        captureButton = findViewById(R.id.captureButton)
        img_back_mt = findViewById(R.id.id_back_mt)
        progressBar = findViewById(R.id.progressBarMT)

        // Khởi tạo executor cho camera
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Kiểm tra quyền truy cập camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            startCamera()
        }

        val idNguoiDung = intent.getStringExtra("idNguoiDung")

        captureButton.setOnClickListener { takePhoto(idNguoiDung?: "") }

        img_back_mt.setOnClickListener{
            val intent1 = Intent(this, CccdGuide::class.java)
            startActivity(intent1)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Không thể khởi động camera: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Camera error: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(idNguoiDung: String) {
        val imageCapture = imageCapture ?: return

        captureButton.isEnabled = false

        val progressBar = findViewById<ProgressBar>(R.id.progressBarMT)
        progressBar.visibility = View.VISIBLE

        outputFile = File(filesDir, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile!!).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(outputFile!!.absolutePath)

                capturedImageView.visibility = View.VISIBLE
                capturedImageView.setImageBitmap(bitmap)

                val handler = android.os.Handler()
                handler.postDelayed({
                    progressBar.visibility = View.GONE
                    captureButton.isEnabled = true
                    scanQRCode(bitmap, idNguoiDung)
                }, 500) // Độ trễ 1 giây
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@CaptureFrontActivity, "Chụp ảnh thất bại: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Capture error: ${exception.message}")
            }
        })
    }

    private fun scanQRCode(bitmap: Bitmap, idNguoiDung: String) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val barcodeScanner = BarcodeScanning.getClient()

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isEmpty()) {
                    showRetryDialog()
                } else {
                    for (barcode in barcodes) {
                        val value = barcode.displayValue
                        if (value != null) {
                            parseQRCodeData(value)

                            if (outputFile != null) {
                                val intent = Intent(this, CaptureBackActivity::class.java)
                                intent.putExtra("frontImagePath", outputFile!!.absolutePath)

                                intent.putExtra("idNguoiDung", idNguoiDung)

                                extractedInfo.forEach { (key, value) -> intent.putExtra(key, value) }
                                startActivity(intent)
                            }

                            break
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Không thể quét mã QR: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Barcode scanning error: ${e.message}")
            }
    }

    private fun parseQRCodeData(data: String) {
        if (data.contains("||")) {
            // Trường hợp không có CMND
            val sections = data.split("||")
            if (sections.size == 2) {
                val cccd = sections[0].trim() // CCCD 12 số
                val remainingData = sections[1].trim() // Thông tin còn lại

                val fields = remainingData.split("|")
                if (fields.size == 5) {
                    extractedInfo["cccd"] = cccd
                    extractedInfo["name"] = fields[0].trim()
                    extractedInfo["birthDate"] = fields[1].trim()
                    extractedInfo["gender"] = fields[2].trim()
                    extractedInfo["address"] = fields[3].trim()
                    extractedInfo["issueDate"] = fields[4].trim()

                    Log.d(TAG, "Extracted Data (without CMND): $extractedInfo")
                    return
                }
            }
        } else if (data.split("|").size == 7) {
            // Trường hợp có CMND
            val fields = data.split("|")
            extractedInfo["cccd"] = fields[0].trim() // CCCD 12 số
            extractedInfo["cmnd"] = fields[1].trim() // CMND 9 số
            extractedInfo["name"] = fields[2].trim() // Tên
            extractedInfo["birthDate"] = fields[3].trim() // Ngày sinh
            extractedInfo["gender"] = fields[4].trim() // Giới tính
            extractedInfo["address"] = fields[5].trim() // Nơi thường trú
            extractedInfo["issueDate"] = fields[6].trim() // Ngày cấp

            Log.d(TAG, "Extracted Data (with CMND): $extractedInfo")
            return
        } else {
            // Báo lỗi nếu không khớp định dạng nào
            Toast.makeText(this, "Dữ liệu mã QR không hợp lệ.", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Invalid QR data format: $data")
        }
    }

    private fun showRetryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null) // Thay bằng file layout của bạn

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val titleTextView = dialogView.findViewById<TextView>(R.id.dialog_title)
        val messageTextView = dialogView.findViewById<TextView>(R.id.dialog_message)

        titleTextView.text = "Thông báo"
        messageTextView.text = "Đảm bảo giấy tờ không bị mờ, tối hoặc chói sáng"

        // Xử lý nút "OK" trong dialog
        val okButton = dialogView.findViewById<TextView>(R.id.dialog_ok_button)
        okButton.setOnClickListener {
            alertDialog.dismiss() // Đóng dialog
            capturedImageView.setImageBitmap(null)
            startCamera() // Bắt đầu lại camera để thử chụp lại
        }

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        capturedImageView.setImageBitmap(null)
        progressBar.visibility = View.GONE
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}