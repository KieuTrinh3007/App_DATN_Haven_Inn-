package com.example.app_datn_haven_inn.ui.cccd

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log

class CaptureBackActivity : AppCompatActivity() {

    private lateinit var previewViewBack: PreviewView
    private lateinit var capturedImageViewBack: ImageView
    private lateinit var captureButtonBack: ImageView
    private lateinit var id_back_ms: ImageView
    private lateinit var progressBar: ProgressBar

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
        id_back_ms = findViewById(R.id.id_back_ms)
        progressBar = findViewById(R.id.progressBarMS)

        // Khởi tạo camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Khởi động camera
        startCamera()

        val idNguoiDung = intent.getStringExtra("idNguoiDung")
        val frontImagePath = intent.getStringExtra("frontImagePath")
        // Nút "Chụp ảnh"
        captureButtonBack.setOnClickListener {
            takePhoto(idNguoiDung!!, frontImagePath)
        }

        id_back_ms.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
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
                Toast.makeText(this, "Không thể khởi động camera: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(idNguoiDung: String, frontImagePath: String?) {
        val imageCapture = imageCapture ?: return

        captureButtonBack.isEnabled = false

        val progressBar = findViewById<ProgressBar>(R.id.progressBarMS)
        progressBar.visibility = View.VISIBLE

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
                    capturedImageViewBack.visibility = View.VISIBLE
                    capturedImageViewBack.setImageBitmap(bitmap)

                    val handler = android.os.Handler()
                    handler.postDelayed({
                        progressBar.visibility = View.GONE
                        captureButtonBack.isEnabled = true
                        extractCCCDData(bitmap, idNguoiDung, frontImagePath)
                    }, 1000) // Độ trễ 1 giây

                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CaptureBackActivity,
                        "Chụp ảnh thất bại: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun extractCCCDData(bitmap: Bitmap, idNguoiDung: String, frontImagePath: String?) {
        val extractor = CCCDDataExtractor()
        extractor.extractCCCDInfo(bitmap) { dataMap ->
            if (dataMap.isNotEmpty()) {
                // Kiểm tra thông tin ngày cấp
                val issueDate = dataMap["issue_date"]
                if (!issueDate.isNullOrEmpty()) {
                    Log.d("ngaycap", "extractCCCDData: " + issueDate)
                    // Nếu tìm thấy ngày cấp, chuyển sang ShowInf  oActivity
                    val intent1 = Intent(this, ShowInfoActivity::class.java)

                    // Truyền ảnh mặt sau và mặt trước
                    outputFile?.let {
                        intent1.putExtra("backImagePath", it.absolutePath)
                    }

                    frontImagePath?.let {
                        intent1.putExtra("frontImagePath", it)
                    }

                    // Truyền thông tin từ CaptureFrontActivity
                    intent.getStringExtra("cccd")?.let { intent1.putExtra("cccd", it) }
                    intent.getStringExtra("name")?.let { intent1.putExtra("name", it) }
                    intent.getStringExtra("birthDate")?.let { intent1.putExtra("birthDate", it) }
                    intent.getStringExtra("gender")?.let { intent1.putExtra("gender", it) }
                    intent.getStringExtra("address")?.let { intent1.putExtra("address", it) }
                    intent.getStringExtra("issueDate")?.let { intent1.putExtra("issueDate", it) }

                    // Truyền thông tin ngày cấp từ mặt sau
//                    intent1.putExtra("issueDateBack", issueDate)
                    intent1.putExtra("idNguoiDung", idNguoiDung)

                    startActivity(intent1)
                } else {
                    // Nếu không tìm thấy ngày cấp, yêu cầu người dùng chụp lại
                    showRetryDialogBack()
                }
            } else {
                // Nếu không có dữ liệu nào, yêu cầu người dùng chụp lại
                showRetryDialogBack()
            }
        }
    }

    private fun showRetryDialogBack() {
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
            capturedImageViewBack.setImageBitmap(null)
            startCamera() // Bắt đầu lại camera để thử chụp lại
        }

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE
        capturedImageViewBack.setImageBitmap(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
