	package com.example.app_datn_haven_inn.ui.auth

	import android.content.Intent
	import android.os.Bundle
	import android.os.CountDownTimer
	import android.widget.EditText
	import android.widget.ImageView
	import android.widget.TextView
	import android.widget.Toast
	import androidx.activity.enableEdgeToEdge
	import androidx.appcompat.app.AppCompatActivity
	import androidx.lifecycle.lifecycleScope
	import com.example.app_datn_haven_inn.R
	import com.example.app_datn_haven_inn.database.CreateService
	import com.example.app_datn_haven_inn.database.service.NguoiDungService
	import kotlinx.coroutines.Dispatchers
	import kotlinx.coroutines.launch
	import kotlinx.coroutines.withContext
	import org.json.JSONException
	import org.json.JSONObject
	import retrofit2.Response

	class Indentity_authentication : AppCompatActivity() {

		private var email: String? = null
		private lateinit var edtOtp: EditText
		private lateinit var btnXacThuc: TextView
		private lateinit var btnGuiLai: TextView
		private lateinit var id_back_indentity: ImageView

		private var countDownTimer: CountDownTimer? = null
		private val countDownTime = 30 * 1000L

		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			setContentView(R.layout.activity_indentity_authentication)
			enableEdgeToEdge()

			// Nhận email từ Intent
			email = intent.getStringExtra("email")

			// Tham chiếu đến trường nhập OTP và các nút
			edtOtp = findViewById(R.id.edt_otp)
			btnXacThuc = findViewById(R.id.edt_xac_thuc)
			btnGuiLai = findViewById(R.id.edt_gui_lai)
			id_back_indentity = findViewById(R.id.id_back_indentity)

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

			id_back_indentity.setOnClickListener{
				val intent = Intent(this, Register::class.java)
				startActivity(intent)
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
				putExtra("email", email)
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
							val errorBody = response.errorBody()?.string()
							val errorMessage = extractMessageFromErrorBody(errorBody)
							Toast.makeText(this@Indentity_authentication, errorMessage, Toast.LENGTH_SHORT).show()
						}
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@Indentity_authentication, "Lỗi khi gửi OTP: ${e.message}", Toast.LENGTH_SHORT).show()
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

		override fun onDestroy() {
			super.onDestroy()
			// Hủy bỏ bộ đếm khi Activity bị hủy
			countDownTimer?.cancel()
		}
	}
