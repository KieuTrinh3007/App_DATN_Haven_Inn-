package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RePassword : AppCompatActivity() {
    private lateinit var oldPass: EditText
    private lateinit var newPass: EditText
    private lateinit var reNewPass: EditText
    private lateinit var saveButton: TextView
    private lateinit var btn_back: ImageView
    private var userId: String = "" // ID người dùng được truyền từ Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_password)

        // Nhận dữ liệu từ Intent
        userId = intent.getStringExtra("idNguoiDung") ?: ""
        Log.d("userId", "onCreate: " + userId)

        // Liên kết View
        oldPass = findViewById(R.id.oldPass)
        newPass = findViewById(R.id.newPass)
        reNewPass = findViewById(R.id.reNewPass)
        saveButton = findViewById(R.id.btn_doimk)
        btn_back = findViewById(R.id.img_back_doiMK)

        saveButton.setOnClickListener { handleChangePassword() }

        btn_back.setOnClickListener{
            finish()
        }

    }

    private fun handleChangePassword() {
        val oldPassword = oldPass.text.toString().trim()
        val newPassword = newPass.text.toString().trim()
        val confirmPassword = reNewPass.text.toString().trim()

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show()
            return
        }

        // Gọi API đổi mật khẩu
        val payload = mapOf(
            "id" to userId,
            "matKhauCu" to oldPassword,
            "matKhauMoi" to newPassword
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authService = CreateService.createService<NguoiDungService>()
                val response = authService.setupPassword(payload)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val message = response.body()?.get("message") ?: "Đổi mật khẩu thành công!"
                        Toast.makeText(this@RePassword, message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RePassword, SignIn::class.java)
                        startActivity(intent)
                        finish() // Quay lại màn hình trước
                    } else {
                        val error = response.errorBody()?.string() ?: "Lỗi xảy ra. Vui lòng thử lại!"
                        Toast.makeText(this@RePassword, error, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RePassword, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
