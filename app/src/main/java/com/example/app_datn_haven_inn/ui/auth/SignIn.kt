package com.example.app_datn_haven_inn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.ui.main.MainActivity
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

    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService<NguoiDungService>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Ánh xạ view
        edtEmail = findViewById(R.id.edt_dangnhap_email)
        edtPassword = findViewById(R.id.edt_dangnhap_pass)
        btnSignIn = findViewById(R.id.btnSignIn)
        btn_forgot_pw = findViewById(R.id.txt_dangnhap_forgot)
        btn_signUp = findViewById(R.id.txtSignUpSignIn)

        // Xử lý sự kiện nút đăng nhập
        btnSignIn.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            } else {
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

    private fun handleLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<Map<String, Any>> = nguoiDungService.login(email, password)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if ((responseBody["status"] as? Double)?.toInt() == 200 && responseBody["userId"] != null) {
                            val userId = responseBody["userId"] as String
                            saveUserToSharedPreferences(userId)
//                            Toast.makeText(this@SignIn, "Đăng nhập thành công!", Toast.LENGTH_SHORT)
//                                .show()
                            navigateToHomeScreen()
                        }
                        else {
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

}
