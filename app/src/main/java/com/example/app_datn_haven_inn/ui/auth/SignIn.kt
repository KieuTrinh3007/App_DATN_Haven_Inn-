package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.ui.main.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SignIn : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignIn: TextView
    private lateinit var btn_forgot_pw: TextView
    private lateinit var btn_signUp: TextView
    private lateinit var passVisible: ImageView
    private lateinit var chkRememberMe: CheckBox

    var isPasswordVisible = false

    // Sử dụng CreateService để tạo NguoiDungService
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService<NguoiDungService>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableEdgeToEdge()

        // Ánh xạ view
        edtEmail = findViewById(R.id.edt_dangnhap_email)
        edtPassword = findViewById(R.id.edt_dangnhap_pass)
        btnSignIn = findViewById(R.id.btnSignIn)
        btn_forgot_pw = findViewById(R.id.txt_dangnhap_forgot)
        btn_signUp = findViewById(R.id.txtSignUpSignIn)
        passVisible = findViewById(R.id.passVisible)
        chkRememberMe = findViewById(R.id.checkbox_remember_me)

        loadLoginDetails()
        fetchDeviceToken()

        passVisible.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Hiển thị mật khẩu
                edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passVisible.setImageResource(R.drawable.nohide) // Đổi icon thành "hide"
            } else {
                // Ẩn mật khẩu
                edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passVisible.setImageResource(R.drawable.hide) // Đổi icon thành "nohide"
            }

        }

        // Xử lý sự kiện nút đăng nhập
        btnSignIn.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            } else {
                if (chkRememberMe.isChecked) {
                    saveLoginDetails(email, password)
                } else {
                    clearLoginDetails()
                }
                handleLogin(email, password)
            }
        }


        btn_forgot_pw.setOnClickListener{
            val intent1 = Intent(this, Forgot_password::class.java)
            startActivity(intent1)
        }

        btn_signUp.setOnClickListener{
            val intent2 = Intent(this, Register::class.java)
            startActivity(intent2)
        }
    }

    private fun saveLoginDetails(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", true)
        editor.apply()
    }

    private fun clearLoginDetails() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun loadLoginDetails() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)

        if (rememberMe) {
            edtEmail.setText(email)
            edtPassword.setText(password)
            chkRememberMe.isChecked = true
        }
    }

    private fun handleLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val deviceToken = getDeviceToken() ?: ""
                val response: Response<Map<String, Any>> = nguoiDungService.login(email, password, deviceToken)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if ((responseBody["status"] as? Double)?.toInt() == 200 && responseBody["userId"] != null) {
                            val userId = responseBody["userId"] as String
                            saveUserToSharedPreferences(userId)
                            navigateToHomeScreen()
                        } else {
                            val errorMessage = responseBody["message"] as? String
                            Toast.makeText(
                                this@SignIn,
                                errorMessage ?: "Tên tài khoản hoặc mật khẩu không chính xác",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SignIn,
                            "Tên tài khoản hoặc mật khẩu không chính xác",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SignIn,
                        "Lỗi kết nối server: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    // Lưu thông tin userId vào SharedPreferences
    private fun saveUserToSharedPreferences(userId: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("idNguoiDung", userId)
        editor.apply()
    }

    // Điều hướng sang màn hình chính
    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchDeviceToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    saveDeviceToken(token)
                    println("Device Token: $token") // Debug log token
                } else {
                    println("Failed to fetch device token: ${task.exception?.message}")
                }
            }
    }

    private fun saveDeviceToken(token: String?) {
        val sharedPreferences = getSharedPreferences("DevicePrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("deviceToken", token)
        editor.apply()
    }

    private fun getDeviceToken(): String? {
        val sharedPreferences = getSharedPreferences("DevicePrefs", MODE_PRIVATE)
        return sharedPreferences.getString("deviceToken", null)
    }


}
