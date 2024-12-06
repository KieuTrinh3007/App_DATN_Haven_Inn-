package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class Forgot_password : AppCompatActivity() {

    private lateinit var edtEmailForgot: EditText
    private lateinit var btnDangNhapSignIn: TextView
    private lateinit var id_back_forgot: ImageView
    private lateinit var nguoiDungService: NguoiDungService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtEmailForgot = findViewById(R.id.edt_forgot_email)
        btnDangNhapSignIn = findViewById(R.id.btn_dangnhap_sign_in)
        id_back_forgot = findViewById(R.id.id_back_forgot)

        nguoiDungService = CreateService.createService()

        // Gán sự kiện click cho nút "Gửi OTP"
        btnDangNhapSignIn.setOnClickListener {
            val email = edtEmailForgot.text.toString()
            if (email.isNotEmpty()) {
                sendOtpToEmail(email)
            } else {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
            }
        }

        id_back_forgot.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    // Gửi OTP qua email
    private fun sendOtpToEmail(email: String) {
        lifecycleScope.launch {
            try {
                // Gọi API gửi OTP từ server
                val response = nguoiDungService.forgotPass(mapOf("email" to email))
                if (response.isSuccessful) {
                    showVerificationDialog()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractMessageFromErrorBody(errorBody)
                    Toast.makeText(this@Forgot_password, errorMessage, Toast.LENGTH_SHORT).show()                }
            } catch (e: HttpException) {
                Toast.makeText(this@Forgot_password, "Lỗi mạng: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@Forgot_password, "Lỗi không xác định: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hiển thị Dialog để nhập OTP
    private var dialog: AlertDialog? = null // Biến lưu trữ dialog để có thể đóng nó khi cần

    private fun showVerificationDialog() {
        if (dialog != null && dialog!!.isShowing) {
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_verification_code, null)
        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val btnSubmit = dialogView.findViewById<TextView>(R.id.btn_submit_otp_forgot)
        val btnResend = dialogView.findViewById<TextView>(R.id.btn_resend_otp_forgot)
        val otpInput = dialogView.findViewById<EditText>(R.id.et_otp_input)

        // Đếm ngược 30 giây để gửi lại OTP
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btnResend.text = "Gửi lại (${millisUntilFinished / 1000}s)"
                btnResend.isEnabled = false
                btnResend.setTextColor(ContextCompat.getColor(this@Forgot_password, R.color.gray))
            }

            override fun onFinish() {
                btnResend.text = "Gửi lại mã"
                btnResend.isEnabled = true
                btnResend.setTextColor(ContextCompat.getColor(this@Forgot_password, R.color.white))
            }
        }
        timer.start()

        btnResend.setOnClickListener {
            val email = edtEmailForgot.text.toString()
            sendOtpToEmail(email)
            btnResend.isEnabled = false
            timer.start()
        }

        // Xử lý khi người dùng nhập OTP và nhấn nút "Submit"
        btnSubmit.setOnClickListener {
            val otp = otpInput.text.toString()
            if (otp.length == 6) {
                verifyOtp(otp, dialog, timer)
            } else {
                otpInput.error = "Vui lòng nhập mã OTP đầy đủ!"
            }
        }

        dialog?.setOnDismissListener { timer.cancel() }
        dialog?.show()
    }

    private fun verifyOtp(otp: String, dialog: AlertDialog?, timer: CountDownTimer) {
        lifecycleScope.launch {
            try {
                val email = edtEmailForgot.text.toString()
                Log.d("emailcheck", "verifyOtp: $email $otp")
                val response = nguoiDungService.verifyOtp(mapOf("email" to email, "otpCode" to otp))
                if (response.isSuccessful) {
                    val message = response.body()?.get("message")?.toString()
                    Toast.makeText(this@Forgot_password, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Forgot_password, SetUpPassword::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    dialog?.dismiss()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractMessageFromErrorBody(errorBody)
                    Toast.makeText(this@Forgot_password, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(this@Forgot_password, "Lỗi xác thực OTP: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@Forgot_password, "Lỗi không xác định: ${e.message}", Toast.LENGTH_SHORT).show()
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
