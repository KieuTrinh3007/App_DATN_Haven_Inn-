package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class Indentity_authentication : AppCompatActivity() {

	private lateinit var firebaseAuth: FirebaseAuth
	private var verificationId: String? = null
	private var phone: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_indentity_authentication)

		firebaseAuth = FirebaseAuth.getInstance()

		// Nhận verificationId và phone từ Intent
		verificationId = intent.getStringExtra("verificationId")
		phone = intent.getStringExtra("phone")

		val otpInputs = listOf(
			findViewById<EditText>(R.id.otpDigit1),
			findViewById<EditText>(R.id.otpDigit2),
			findViewById<EditText>(R.id.otpDigit3),
			findViewById<EditText>(R.id.otpDigit4)
		)

		val btnVerify = findViewById<TextView>(R.id.edt_gui_lai)

		// Xử lý sự kiện khi người dùng nhấn "Xác thực OTP"
		btnVerify.setOnClickListener {
			val otpCode = otpInputs.joinToString("") { it.text.toString() }
			if (otpCode.length == 4) {
				verifyOtp(otpCode)
			} else {
				Toast.makeText(this, "Vui lòng nhập đủ mã OTP!", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun verifyOtp(otpCode: String) {
		if (verificationId != null) {
			val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode)
			firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show()
						// Chuyển sang màn hình chính hoặc lưu thông tin đăng ký
						val intent = Intent(this, Register::class.java) // Chuyển sang màn hình chính
						startActivity(intent)
						finish()
					} else {
						Toast.makeText(this, "Xác thực thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
					}
				}
		} else {
			Toast.makeText(this, "Không thể xác thực mã OTP, vui lòng thử lại!", Toast.LENGTH_SHORT).show()
		}
	}
}
