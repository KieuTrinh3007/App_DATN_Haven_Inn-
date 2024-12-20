package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class SignUp : AppCompatActivity() {

	private lateinit var edtName: EditText
	private lateinit var edtEmail: EditText
	private lateinit var edtPassword: EditText
	private lateinit var btnSignUp: TextView
	private lateinit var id_back_signup: ImageView
	private lateinit var txt_dangnhap_sign_up: TextView
	private lateinit var userService: NguoiDungService

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)
		enableEdgeToEdge()

		// Ánh xạ view
		edtName = findViewById(R.id.edt_dk_name)
		edtEmail = findViewById(R.id.edt_xacnhan_email)
		edtPassword = findViewById(R.id.edt_dk_pass)
		btnSignUp = findViewById(R.id.btn_dangky)
		txt_dangnhap_sign_up = findViewById(R.id.txt_dangnhap_sign_up)
		id_back_signup = findViewById(R.id.id_back_signup)

		// Tạo service để kết nối API
		userService = CreateService.createService()

		val receivedEmail = intent.getStringExtra("email")
		if (receivedEmail != null) {
			edtEmail.setText(receivedEmail)
			edtEmail.isFocusable = false // Ngăn chỉnh sửa
			edtEmail.isCursorVisible = false
		}

		txt_dangnhap_sign_up.setOnClickListener {
			val intent = Intent(this, SignIn::class.java)
			startActivity(intent)
			finish()
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

		id_back_signup.setOnClickListener{
			val intent = Intent(this, Register::class.java)
			startActivity(intent)
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
					xacMinh = false,
					trangThai = true)
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
						val errorBody = response.errorBody()?.string()
						val errorMessage = extractMessageFromErrorBody(errorBody)
						Toast.makeText(this@SignUp, errorMessage, Toast.LENGTH_SHORT).show()
					}
				}
			} catch (e: Exception) {
				withContext(Dispatchers.Main) {
					Toast.makeText(this@SignUp, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private fun extractMessageFromErrorBody(errorBody: String?): String {
		return try {
			if (!errorBody.isNullOrEmpty()) {
				val jsonObject = JSONObject(errorBody)
				jsonObject.optString("message", "Có lỗi xảy ra") // Lấy giá trị "message" hoặc chuỗi mặc định
			} else {
				"Có lỗi xảy ra"
			}
		} catch (e: JSONException) {
			"Lỗi phân tích phản hồi từ server"
		}
	}
}
