package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.database.CreateService
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class SetUpPassword : AppCompatActivity() {

    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnSubmit: TextView
    private lateinit var email: String
    private lateinit var passVisible: ImageView
    private lateinit var passVisible1: ImageView

    var isPasswordVisible = false
    var isPasswordVisible1 = false

    private lateinit var nguoiDungService: NguoiDungService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up_password)  // Đảm bảo bạn đã tạo layout cho Activity này

        edtNewPassword = findViewById(R.id.edt_new_password)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password)
        btnSubmit = findViewById(R.id.btn_submit_new_password)
        passVisible = findViewById(R.id.passVisible)
        passVisible1 = findViewById(R.id.passVisible1)

        nguoiDungService = CreateService.createService()

        // Lấy email từ Intent
        email = intent.getStringExtra("email") ?: ""

        btnSubmit.setOnClickListener {
            val newPassword = edtNewPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

            // Kiểm tra xem mật khẩu và xác nhận có khớp không
            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới và xác nhận mật khẩu!", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
            } else {
                // Gọi hàm setUpPassword để cập nhật mật khẩu
                setUpPassword(newPassword)
            }
        }

        hidepass()
    }

    private fun hidepass(){
        passVisible.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Hiển thị mật khẩu
                edtNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible.setImageResource(R.drawable.nohide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                edtNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible.setImageResource(R.drawable.hide) // Đổi icon thành "nohide"
            }

        }

        passVisible1.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1

            if (isPasswordVisible1) {
                // Hiển thị mật khẩu
                edtConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible1.setImageResource(R.drawable.nohide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible1.setImageResource(R.drawable.hide) // Đổi icon thành "nohide"
            }

        }
    }
    private fun setUpPassword(newPassword: String) {
        lifecycleScope.launch {
            try {
                val response = nguoiDungService.setUpPass(mapOf("email" to email, "matKhauMoi" to newPassword))
                if (response.isSuccessful) {
                    Toast.makeText(this@SetUpPassword, "Mật khẩu đã được cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    // Quay lại Activity trước đó hoặc đóng Activity này
                    val intent = Intent(this@SetUpPassword, SignIn::class.java)
                    startActivity(intent

                    )
                } else {
                    Toast.makeText(this@SetUpPassword, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SetUpPassword, "Lỗi khi cập nhật mật khẩu: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
