package com.example.app_datn_haven_inn.ui.cccd

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureBackActivity : AppCompatActivity() {

    private lateinit var previewViewBack: PreviewView
    private lateinit var capturedImageViewBack: ImageView
    private lateinit var captureButtonBack: Button
    private lateinit var continueButtonBack: Button
    private lateinit var retryButtonBack: Button

    private var imageCapture: ImageCapture? = null
    private var outputFile: File? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_back)

        // Ánh xạ các View
        previewViewBack = findViewById(R.id.previewViewBack)
        capturedImageViewBack = findViewById(R.id.capturedImageViewBack)
        captureButtonBack = findViewById(R.id.captureButtonBack)
        continueButtonBack = findViewById(R.id.continue_buttonBack)
        retryButtonBack = findViewById(R.id.retryButtonBack)

        // Ẩn các nút "Chụp lại" và "Tiếp tục" ban đầu
        continueButtonBack.visibility = Button.GONE
        retryButtonBack.visibility = Button.GONE

        // Khởi tạo camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Khởi động camera
        startCamera()

        // Nút "Chụp ảnh"
        captureButtonBack.setOnClickListener {
            takePhoto()
        }

        // Nút "Chụp lại"
        retryButtonBack.setOnClickListener {
            resetUIBack()
            startCamera() // Khởi động lại camera
        }

        val frontImagePath = intent.getStringExtra("frontImagePath")

        // Nút "Tiếp tục"
        continueButtonBack.setOnClickListener {
            val intent1 = Intent(this, ShowInfoActivity::class.java)
//
            // Gửi đường dẫn ảnh mặt sau qua Intent
            if (outputFile != null) {
                intent1.putExtra("backImagePath", outputFile!!.absolutePath)
            }

            // Nếu có ảnh mặt trước (từ activity khác), bạn cũng có thể gửi qua
            if (!frontImagePath.isNullOrEmpty()) {
                intent1.putExtra("frontImagePath", frontImagePath)
            }

            // Mở activity ShowInfoActivity
            startActivity(intent1)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewViewBack.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
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
                    capturedImageViewBack.setImageBitmap(bitmap)
                    updateUIForCapturedImage()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CaptureBackActivity, "Chụp ảnh thất bại: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun updateUIForCapturedImage() {
        capturedImageViewBack.visibility = ImageView.VISIBLE
        previewViewBack.visibility = PreviewView.GONE
        retryButtonBack.visibility = Button.VISIBLE
        continueButtonBack.visibility = Button.VISIBLE
        captureButtonBack.visibility = Button.GONE
    }

    private fun resetUIBack() {
        // Reset lại UI để chuẩn bị chụp ảnh mới
        capturedImageViewBack.setImageBitmap(null)
        capturedImageViewBack.visibility = ImageView.GONE
        previewViewBack.visibility = PreviewView.VISIBLE
        retryButtonBack.visibility = Button.GONE
        continueButtonBack.visibility = Button.GONE
        captureButtonBack.visibility = Button.VISIBLE
        captureButtonBack.text = "Chụp ảnh"
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
