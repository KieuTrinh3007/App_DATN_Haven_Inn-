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
import com.example.app_datn_haven_inn.ui.home.HomeFragment
import com.example.app_datn_haven_inn.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SignIn : AppCompatActivity() {

	private lateinit var edtPhone: EditText
	private lateinit var edtPassword: EditText
	private lateinit var btnSignIn: TextView

	private val nguoiDungService: NguoiDungService by lazy {
		CreateService.createService()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		// Ánh xạ view
		edtPhone = findViewById(R.id.edt_dangnhap_sdt)
		edtPassword = findViewById(R.id.edt_dangnhap_pass)
		btnSignIn = findViewById(R.id.btnSignIn)

		// Xử lý khi người dùng bấm nút "Đăng nhập"
		btnSignIn.setOnClickListener {
			val phone = edtPhone.text.toString().trim()
			val password = edtPassword.text.toString().trim()

			if (phone.isNotEmpty() && password.isNotEmpty()) {
				login(phone, password)
			} else {
				Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun login(phone: String, password: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				// Thực hiện gọi API
				val response: Response<List<NguoiDungModel>> = nguoiDungService.getListNguoiDung()

				withContext(Dispatchers.Main) {
					if (response.isSuccessful) {
						val users = response.body()
						val user = users?.find { it.soDienThoai == phone && it.matKhau == password }

						if (user != null) {
							Toast.makeText(this@SignIn, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
							navigateToHomeScreen(user)
						} else {
							Toast.makeText(this@SignIn, "Số điện thoại hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show()
						}
					} else {
						Toast.makeText(
							this@SignIn,
							"Lỗi kết nối: ${response.message()}",
							Toast.LENGTH_SHORT
						).show()
					}
				}
			} catch (e: Exception) {
				withContext(Dispatchers.Main) {
					Toast.makeText(this@SignIn, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private fun navigateToHomeScreen(user: NguoiDungModel) {
		// Lưu idNguoiDung vào SharedPreferences
		val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
		val editor = sharedPreferences.edit()
		editor.putString("idNguoiDung", user.id)
		editor.apply()

		// Chuyển đến MainActivity
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
		finish()
	}

}