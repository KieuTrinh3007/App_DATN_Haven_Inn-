package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

	private lateinit var edtName: EditText
	private lateinit var edtEmail: EditText
	private lateinit var edtPassword: EditText
	private lateinit var btnSignUp: TextView
	private lateinit var userService: NguoiDungService

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		// Ánh xạ view
		edtName = findViewById(R.id.edt_dk_name)
		edtEmail = findViewById(R.id.edt_xacnhan_email)
		edtPassword = findViewById(R.id.edt_dk_pass)
		btnSignUp = findViewById(R.id.btn_dangky)

		// Tạo service để kết nối API
		userService = CreateService.createService()

		val receivedEmail = intent.getStringExtra("email")
		if (receivedEmail != null) {
			edtEmail.setText(receivedEmail)
			edtEmail.isFocusable = false // Ngăn chỉnh sửa
			edtEmail.isCursorVisible = false
		}

		btnSignUp.setOnClickListener {
			val name = edtName.text.toString().trim()
			val email = edtEmail.text.toString().trim()
			val password = edtPassword.text.toString().trim()

			// Kiểm tra input trước khi call API
			if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
				Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			// Gọi hàm đăng ký
			registerUser(name, receivedEmail!!, password)
		}
	}

	private fun registerUser(name: String, email: String, password: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val newUser = NguoiDungModel(
					tenNguoiDung = name,
					soDienThoai = "", // Mặc định rỗng
					matKhau = password,
					email = email,
					hinhAnh = null,
					hinhAnhID = "",
					chucVu = 0,
					trangThai = true,
					cccd = ""
				)

				// Gọi API từ service
				val response = userService.registerNguoiDung(newUser)

				// Xử lý kết quả trả về trên UI thread
				withContext(Dispatchers.Main) {
					if (response.isSuccessful) {
						Toast.makeText(this@SignUp, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()

						val intent = Intent(this@SignUp, SignIn::class.java)
						startActivity(intent)
						finish()
					} else {
						val errorMessage = response.errorBody()?.string() ?: "Lỗi không xác định"
						Toast.makeText(this@SignUp, "Đăng ký thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
					}
				}
			} catch (e: Exception) {
				withContext(Dispatchers.Main) {
					Toast.makeText(this@SignUp, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

}
