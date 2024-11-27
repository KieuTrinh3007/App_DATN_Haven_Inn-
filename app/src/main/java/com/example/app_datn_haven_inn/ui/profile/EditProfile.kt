package com.example.app_datn_haven_inn.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class EditProfile : AppCompatActivity() {
    private lateinit var nguoiDungService: NguoiDungService
    private var idNguoiDung: String? = null

    private lateinit var imageViewAvatar: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewEmail: EditText
    private lateinit var textViewPhone: EditText
    private lateinit var btSaveChanges: TextView
    private lateinit var btn_back_edit: ImageView

    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null

    private var oldMatKhau: String = ""
    private var oldChucVu: String = ""
    private var oldTrangThai: String = "true"
    private var oldCccd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        imageViewAvatar = findViewById(R.id.avatarImageView)
        editTextName = findViewById(R.id.nameEditText)
        textViewEmail = findViewById(R.id.emailEditText)
        textViewPhone = findViewById(R.id.phoneTextView)
        btSaveChanges = findViewById(R.id.saveButton)
        btn_back_edit = findViewById(R.id.btn_back_editprofile)

        nguoiDungService = CreateService.createService()

        // Get user ID from intent
        idNguoiDung = intent.getStringExtra("idNguoiDung")
        idNguoiDung?.let {
            loadUserInfo(it)
        }

        imageViewAvatar.setOnClickListener {
            openGallery()
        }

        btSaveChanges.setOnClickListener {
            idNguoiDung?.let { id ->
                saveUserProfile(id)
            }
        }

        btn_back_edit.setOnClickListener{
            finish()
        }
    }

    private fun loadUserInfo(id: String) {
        lifecycleScope.launch {
            try {
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == id }
                    user?.let {
                        editTextName.setText(it.tenNguoiDung)
                        textViewEmail.setText(it.email)
                        textViewPhone.setText(it.soDienThoai)
                        currentImageUrl = it.hinhAnh
                        oldMatKhau = it.matKhau ?: ""
                        oldChucVu = it.chucVu?.toString() ?: "0"
                        oldTrangThai = it.trangThai?.toString() ?: "true"
                        oldCccd = it.cccd ?: ""

                        // Nếu có URL của ảnh, tải ảnh về
                        if (!it.hinhAnh.isNullOrEmpty()) {
                            Glide.with(this@EditProfile)
                                .load(it.hinhAnh)
                                .into(imageViewAvatar)
                        }
                    }
                } else {
                    Toast.makeText(this@EditProfile, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfile, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserProfile(id: String) {
        val name = editTextName.text.toString().trim()
        val sdt = textViewPhone.text.toString().trim()

        if (name.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = name.toRequestBody()
        val sdtBody = sdt.toRequestBody()

        val imagePart = selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri)
            if (file != null) {
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestBody)
            } else {
                Toast.makeText(this, "Không thể xử lý tệp ảnh", Toast.LENGTH_SHORT).show()
                null
            }
        }

        lifecycleScope.launch {
            try {
                val response = nguoiDungService.updateNguoiDung(
                    id = id,
                    tenNguoiDung = nameBody,
                    soDienThoai = sdtBody,
                    matKhau = oldMatKhau.toRequestBody(),
                    email = textViewEmail.text.toString().toRequestBody(),
                    chucVu = oldChucVu.toRequestBody(),
                    trangThai = oldTrangThai.toRequestBody(),
                    image = imagePart
                )
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfile, "Cập nhật thành công", Toast.LENGTH_SHORT).show()

                    // Trả kết quả về ProfileFragment
                    val resultIntent = Intent()
                    resultIntent.putExtra("updated", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish() // Kết thúc Activity và quay lại ProfileFragment
                } else {
                    Toast.makeText(this@EditProfile, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfile, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            // Cập nhật lại ImageView với ảnh đã chọn
            imageViewAvatar.setImageURI(selectedImageUri)

            // Kiểm tra nếu ảnh đã được chọn thì sử dụng Glide để tải ảnh từ URI
            Glide.with(this)
                .load(selectedImageUri)
                .into(imageViewAvatar)
        }
    }

    // Get file from URI for image upload
    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val fileName = getFileName(uri)
            val file = File(cacheDir, fileName)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Get file name from URI
    private fun getFileName(uri: Uri): String {
        var name = "temp_image"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
}
