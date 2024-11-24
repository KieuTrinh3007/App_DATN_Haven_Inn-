package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnDangKy = findViewById<TextView>(R.id.btn_tieptheo)
        val edtEmail = findViewById<EditText>(R.id.edt_dkk_email) // Email người dùng

        btnDangKy.setOnClickListener {
            val email = edtEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gọi phương thức gửi OTP
            sendOtp(email)
        }
    }

    // Phương thức gửi OTP
    private fun sendOtp(email: String) {
        val service = CreateService.createService<NguoiDungService>()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response: Response<Map<String, String>> = service.sendOtp(email)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Register, "OTP đã được gửi tới $email", Toast.LENGTH_SHORT).show()

                        // Chuyển sang màn hình xác thực OTP và truyền email
                        val intent = Intent(this@Register, Indentity_authentication::class.java).apply {
                            putExtra("email", email)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Register, "Gửi OTP không thành công: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Register, "Lỗi khi gửi OTP: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
