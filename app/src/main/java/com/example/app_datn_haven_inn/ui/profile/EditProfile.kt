package com.example.app_datn_haven_inn.ui.profile

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfile : AppCompatActivity() {
    private lateinit var nguoiDungService: NguoiDungService
    private var idNguoiDung: String? = null

    private lateinit var imageViewAvatar: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewEmail: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var btSaveChanges: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        imageViewAvatar = findViewById(R.id.imageViewAvatar)
        editTextName = findViewById(R.id.Name)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewPhone = findViewById(R.id.textViewPhone)
        btSaveChanges = findViewById(R.id.bt_save_changes)

        nguoiDungService = CreateService.createService()

        // Get user ID from intent
        idNguoiDung = intent.getStringExtra("idNguoiDung")
        idNguoiDung?.let {
            loadUserInfo(it)
        }

        btSaveChanges.setOnClickListener {
            updateUserInfo()
        }
    }

    private fun loadUserInfo(id: String) {
        lifecycleScope.launch {
            try {
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == id }
                    user?.let { populateFields(it) }
                } else {
                    Toast.makeText(this@EditProfile, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfile, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateFields(user: NguoiDungModel) {
        editTextName.setText(user.tenNguoiDung)
        textViewEmail.text = user.email
        textViewPhone.text = user.soDienThoai
        Glide.with(this).load(user.hinhAnh).into(imageViewAvatar)
    }

    private fun updateUserInfo() {
        val name = editTextName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show()
            return
        }

        idNguoiDung?.let { id ->
            lifecycleScope.launch {
                try {
                    // Lấy đường dẫn ảnh (nếu có) từ người dùng, nếu không sẽ giữ ảnh cũ
                    val selectedImageFilePath: String? = null // Bạn có thể thay thế bằng đường dẫn từ người dùng chọn ảnh, ví dụ: từ Gallery

                    val imagePart = prepareImageFilePart(selectedImageFilePath)

                    val response = nguoiDungService.updateNguoiDung(
                        id = id,
                        tenNguoiDung = name.toRequestBody(),
                        soDienThoai = "".toRequestBody(), // giữ nguyên số điện thoại
                        matKhau = "".toRequestBody(), // giữ nguyên mật khẩu
                        email = "".toRequestBody(), // giữ nguyên email
                        chucVu = "".toRequestBody(), // giữ nguyên chức vụ
                        trangThai = "true".toRequestBody(),
                        image = imagePart ?: MultipartBody.Part.createFormData("hinhAnh", "") // Nếu không có ảnh mới, truyền giá trị rỗng
                    )

                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfile, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditProfile, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfile, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun prepareImageFilePart(filePath: String?): MultipartBody.Part? {
        if (filePath.isNullOrEmpty()) {
            return null // Nếu không có ảnh được chọn, trả về null để giữ nguyên ảnh cũ
        }
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("hinhAnh", file.name, requestFile)
    }


    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())
}
