package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Register : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnDangKy = findViewById<TextView>(R.id.btn_dangky)
        val edtPhone = findViewById<EditText>(R.id.edt_dkk_sdt)

        btnDangKy.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            if (phone.isNotEmpty() && phone.length == 10) {
                val fullPhone = "+84$phone"
                sendOtp(fullPhone)
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOtp(phone: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)       // Số điện thoại người dùng nhập
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)           // Activity hiện tại
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Xác thực tự động thành công, đăng nhập trực tiếp
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@Register, "Gửi OTP thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    this@Register.verificationId = verificationId

                    val intent = Intent(this@Register, Indentity_authentication::class.java)
                    intent.putExtra("verificationId", verificationId) // Truyền verificationId
                    intent.putExtra("phone", phone) // Truyền số điện thoại
                    startActivity(intent)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
