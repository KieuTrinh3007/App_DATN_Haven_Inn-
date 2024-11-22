package com.example.app_datn_haven_inn.ui.profile

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfile : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneTextView: TextView
    private lateinit var avatarImageView: ImageView
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Khởi tạo các view
        nameEditText = findViewById(R.id.Name)
        emailEditText = findViewById(R.id.textViewEmail)
        phoneTextView = findViewById(R.id.textViewPhone)
        avatarImageView = findViewById(R.id.imageViewAvatar)

        // Lấy idNguoiDung từ Intent
        val idNguoiDung = intent.getStringExtra("idNguoiDung")
        idNguoiDung?.let {
            fetchUserProfile(it)
        }

        // Thiết lập padding cho hệ thống thanh trạng thái và thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Lấy thông tin người dùng từ API
    private fun fetchUserProfile(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == id }
                    user?.let { updateUI(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Cập nhật giao diện với dữ liệu người dùng
    private suspend fun updateUI(user: NguoiDungModel) {
        withContext(Dispatchers.Main) {
            // Cập nhật thông tin vào các trường
            nameEditText.setText(user.tenNguoiDung)  // Cập nhật tên
            emailEditText.setText(user.email)  // Cập nhật email
            phoneTextView.text = user.soDienThoai  // Cập nhật số điện thoại
            Glide.with(this@EditProfile)
                .load(user.hinhAnh)  // Hiển thị ảnh đại diện
                .into(avatarImageView)
        }
    }
}
