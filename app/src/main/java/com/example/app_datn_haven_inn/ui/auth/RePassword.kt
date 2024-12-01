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
    private lateinit var passVisible: ImageView
    private lateinit var passVisible1: ImageView
    private lateinit var passVisible2: ImageView

    var isPasswordVisible = false
    var isPasswordVisible1 = false
    var isPasswordVisible2 = false
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
        passVisible = findViewById(R.id.passVisible)
        passVisible1 = findViewById(R.id.passVisible1)
        passVisible2 = findViewById(R.id.passVisible2)


        // Thiết lập sự kiện click cho nút Lưu
        saveButton.setOnClickListener { handleChangePassword() }

        btn_back.setOnClickListener{
            finish()
        }

        hidepass()
    }

    private fun hidepass(){
        passVisible.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Hiển thị mật khẩu
                oldPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible.setImageResource(R.drawable.hide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                oldPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible.setImageResource(R.drawable.nohide) // Đổi icon thành "nohide"
            }

            // Đặt con trỏ về cuối dòng
            oldPass.setSelection(oldPass.text.length)
        }

        passVisible1.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1

            if (isPasswordVisible1) {
                // Hiển thị mật khẩu
                newPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible1.setImageResource(R.drawable.hide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                newPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible1.setImageResource(R.drawable.nohide) // Đổi icon thành "nohide"
            }

            // Đặt con trỏ về cuối dòng
            newPass.setSelection(newPass.text.length)
        }

        passVisible2.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2

            if (isPasswordVisible2) {
                // Hiển thị mật khẩu
                reNewPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible2.setImageResource(R.drawable.hide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                reNewPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible2.setImageResource(R.drawable.nohide) // Đổi icon thành "nohide"
            }

            // Đặt con trỏ về cuối dòng
            reNewPass.setSelection(reNewPass.text.length)
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
