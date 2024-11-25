package com.example.app_datn_haven_inn.ui.cccd

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import java.io.File
import java.util.concurrent.ExecutorService

class CaptureFrontActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var capturedImageView: ImageView
    private lateinit var captureButton: Button
    private lateinit var retryButton: Button
    private lateinit var continueButton: Button

    private var imageCapture: ImageCapture? = null
    private var outputFile: File? = null
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_front)

        // Ánh xạ các View
        previewView = findViewById(R.id.previewView)
        capturedImageView = findViewById(R.id.capturedImage)
        captureButton = findViewById(R.id.captureButton)
        retryButton = findViewById(R.id.retryButton)
        continueButton = findViewById(R.id.continue_button)

        // Kiểm tra quyền và khởi động camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            startCamera()
        }

        // Nút "Chụp ảnh"
        captureButton.setOnClickListener {
            takePhoto()
        }

        // Nút "Chụp lại"
        retryButton.setOnClickListener {
            resetUIFront()
            startCamera()
        }

        // Nút "Tiếp tục"
        continueButton.setOnClickListener {
            if (outputFile != null) {
                val intent = Intent(this, CaptureBackActivity::class.java)
                intent.putExtra("frontImagePath", outputFile!!.absolutePath)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Chưa có ảnh để chuyển tiếp", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this, "Không thể khởi động camera: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Tạo tệp lưu ảnh mới mỗi khi chụp ảnh
        outputFile = File(filesDir, "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile!!).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Chuyển tệp thành Bitmap và hiển thị
                    val bitmap = BitmapFactory.decodeFile(outputFile!!.absolutePath)
                    capturedImageView.setImageBitmap(bitmap)
                    updateUIForCapturedImage()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CaptureFrontActivity,
                        "Chụp ảnh thất bại: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

    }

    private fun updateUIForCapturedImage() {
        capturedImageView.visibility = ImageView.VISIBLE
        previewView.visibility = PreviewView.GONE
        retryButton.visibility = Button.VISIBLE
        continueButton.visibility = Button.VISIBLE
        captureButton.visibility = Button.GONE
    }

    private fun resetUIFront() {
        capturedImageView.setImageBitmap(null)
        capturedImageView.visibility = ImageView.GONE

        // Hiển thị lại preview view và ẩn các nút đã chụp
        previewView.visibility = PreviewView.VISIBLE

        // Ẩn nút "Chụp lại" và "Tiếp tục" sau khi reset UI
        retryButton.visibility = Button.GONE
        continueButton.visibility = Button.GONE

        captureButton.visibility = Button.VISIBLE
        captureButton.text = "Chụp ảnh"
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền Camera để hoạt động", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
