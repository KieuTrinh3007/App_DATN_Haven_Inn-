	package com.example.app_datn_haven_inn.ui.auth

	import android.content.Intent
	import android.os.Bundle
	import android.os.CountDownTimer
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

	class Indentity_authentication : AppCompatActivity() {

		private var email: String? = null
		private lateinit var edtOtp: EditText
		private lateinit var btnXacThuc: TextView
		private lateinit var btnGuiLai: TextView

		private var countDownTimer: CountDownTimer? = null
		private val countDownTime = 30 * 1000L // 30 seconds in milliseconds

		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			setContentView(R.layout.activity_indentity_authentication)

			// Nhận email từ Intent
			email = intent.getStringExtra("email")

			// Tham chiếu đến trường nhập OTP và các nút
			edtOtp = findViewById(R.id.edt_otp)
			btnXacThuc = findViewById(R.id.edt_xac_thuc)
			btnGuiLai = findViewById(R.id.edt_gui_lai)

			// Xử lý sự kiện khi bấm nút "Xác thực"
			btnXacThuc.setOnClickListener {
				val otp = edtOtp.text.toString()
				if (otp.isEmpty()) {
					Toast.makeText(this, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show()
				} else if (otp.length != 6) { // Giả sử OTP phải có độ dài 6 ký tự
					Toast.makeText(this, "OTP không hợp lệ", Toast.LENGTH_SHORT).show()
				} else {
					verifyOtp(email ?: "", otp)
				}
			}

			startCountDownTimer()

			// Xử lý sự kiện khi bấm nút "Gửi lại"
			btnGuiLai.setOnClickListener {
				guiLaiOTP(email.toString())
				startCountDownTimer()
			}
		}

		private fun verifyOtp(email: String, otp: String) {
			val service = CreateService.createService<NguoiDungService>()
			val payload = mapOf(
				"email" to email,
				"otpCode" to otp
			)

			lifecycleScope.launch(Dispatchers.IO) {
				try {
					val response = service.verifyOtp(payload)

					withContext(Dispatchers.Main) {
						if (response.isSuccessful && response.body()?.get("message") == "Xác minh OTP thành công") {
							Toast.makeText(this@Indentity_authentication, "Xác thực thành công", Toast.LENGTH_SHORT).show()
							navigateToSignUp()
						} else {
							Toast.makeText(this@Indentity_authentication, "OTP không hợp lệ", Toast.LENGTH_SHORT).show()
						}
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@Indentity_authentication, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
					}
				}
			}
		}

		private fun startCountDownTimer() {
			// Vô hiệu hóa nút gửi lại
			btnGuiLai.isEnabled = false
			btnGuiLai.setBackgroundResource(R.drawable.button_disabled) // Đổi màu nút thành xám

			// Bắt đầu đếm ngược 30 giây
			countDownTimer = object : CountDownTimer(countDownTime, 1000) {
				override fun onTick(millisUntilFinished: Long) {
					// Cập nhật văn bản trên nút gửi lại với thời gian còn lại
					btnGuiLai.text = "Gửi lại ${millisUntilFinished / 1000}s"
				}

				override fun onFinish() {
					// Khi đếm ngược kết thúc, hiển thị lại nút
					btnGuiLai.text = "Gửi lại"
					btnGuiLai.isEnabled = true
					btnGuiLai.setBackgroundResource(R.drawable.khung_button1) // Đổi lại màu nút khi có thể bấm
				}
			}

			countDownTimer?.start()
		}


		private fun navigateToSignUp() {
			val intent = Intent(this, SignUp::class.java).apply {
				putExtra("email", email) // Truyền email qua Intent
			}
			startActivity(intent)
			finish()
		}


		private fun guiLaiOTP(email: String) {
			val service = CreateService.createService<NguoiDungService>()

			lifecycleScope.launch(Dispatchers.IO) {
				try {
					val response: Response<Map<String, String>> = service.sendOtp(email)

					withContext(Dispatchers.Main) {
						if (response.isSuccessful) {
							Toast.makeText(this@Indentity_authentication, "OTP đã được gửi tới $email", Toast.LENGTH_SHORT).show()
						} else {
							Toast.makeText(this@Indentity_authentication, "Gửi OTP không thành công: ${response.message()}", Toast.LENGTH_SHORT).show()
						}
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@Indentity_authentication, "Lỗi khi gửi OTP: ${e.message}", Toast.LENGTH_SHORT).show()
					}
				}
			}
		}

		override fun onDestroy() {
			super.onDestroy()
			// Hủy bỏ bộ đếm khi Activity bị hủy
			countDownTimer?.cancel()
		}

	}
