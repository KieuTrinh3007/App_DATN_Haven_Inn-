package com.example.app_datn_haven_inn.ui.cccd

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.main.MainActivity

class CccdGuide : AppCompatActivity() {
    private lateinit var btn_bat_dau_chup: TextView
    private lateinit var btn_back: ImageView
    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cccd_guide)

        btn_bat_dau_chup = findViewById(R.id.btn_bat_dau_chup)
        btn_back = findViewById(R.id.img_back_xt1)

        // Kiểm tra quyền khi khởi động màn hình
        checkCameraPermission()

        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToFragment", 4) // 4 là vị trí của ProfileFragment
            startActivity(intent)
            finish()
        }

        val idNguoiDung = intent.getStringExtra("idNguoiDung")

        btn_bat_dau_chup.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, CaptureFrontActivity::class.java)
                intent.putExtra("idNguoiDung", idNguoiDung)
                startActivity(intent)
            } else {
                showPermissionDeniedDialog()
            }
        }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }
    private fun showPermissionDeniedDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Quyền truy cập camera bị từ chối")
            .setMessage("Để tiếp tục, bạn cần cấp quyền truy cập camera trong cài đặt.")
            .setPositiveButton("Đi tới cài đặt") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Quyền camera bị từ chối. Vui lòng cấp quyền để sử dụng tính năng này.", Toast.LENGTH_LONG).show()
//        }
//    }
}
