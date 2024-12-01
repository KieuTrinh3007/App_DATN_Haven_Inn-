package com.example.app_datn_haven_inn.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
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
    private lateinit var btnBack: ImageView
    private lateinit var progressBar: View

    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null

    private var oldName = ""
    private var oldPhone = ""
    private var oldMatKhau = ""
    private var oldChucVu = ""
    private var oldTrangThai = "true"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        imageViewAvatar = findViewById(R.id.avatarImageView)
        editTextName = findViewById(R.id.nameEditText)
        textViewEmail = findViewById(R.id.emailEditText)
        textViewPhone = findViewById(R.id.phoneTextView)
        btSaveChanges = findViewById(R.id.saveButton)
        btnBack = findViewById(R.id.btn_back_editprofile)
        progressBar = findViewById(R.id.progressBar)

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
                val name = editTextName.text.toString().trim()
                val phone = textViewPhone.text.toString().trim()

                // Validate input
                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (name == oldName && phone == oldPhone && selectedImageUri == null) {
                    Toast.makeText(this, "Không có thay đổi nào được thực hiện", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                performUpdateProfile(id, name, phone)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadUserInfo(id: String) {
        showLoading(true)
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
                        oldName = it.tenNguoiDung ?: ""
                        oldPhone = it.soDienThoai ?: ""
                        oldMatKhau = it.matKhau ?: ""
                        oldChucVu = it.chucVu?.toString() ?: "0"
                        oldTrangThai = it.trangThai?.toString() ?: "true"

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
            } finally {
                showLoading(false)
            }
        }
    }

    private fun performUpdateProfile(id: String, name: String, phone: String) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val nameBody = name.toRequestBody()
                val phoneBody = phone.toRequestBody()
                val imagePart = prepareImagePart()

                val response = nguoiDungService.updateNguoiDung(
                    id = id,
                    tenNguoiDung = nameBody,
                    soDienThoai = phoneBody,
                    matKhau = oldMatKhau.toRequestBody(),
                    email = textViewEmail.text.toString().toRequestBody(),
                    chucVu = oldChucVu.toRequestBody(),
                    trangThai = oldTrangThai.toRequestBody(),
                    image = imagePart
                )

                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfile, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK, Intent().putExtra("updated", true))
                    finish()
                } else {
                    Toast.makeText(this@EditProfile, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfile, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun prepareImagePart(): MultipartBody.Part? {
        return selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri)
            file?.let {
                val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestBody)
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
            Glide.with(this).load(selectedImageUri).into(imageViewAvatar)
        }
    }

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

    private fun showLoading(isLoading: Boolean) {
        val overlay = findViewById<View>(R.id.overlay) // Overlay mờ
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        overlay.visibility = if (isLoading) View.VISIBLE else View.GONE

        editTextName.isEnabled = !isLoading
        textViewEmail.isEnabled = !isLoading
        textViewPhone.isEnabled = !isLoading
        btSaveChanges.isEnabled = !isLoading
    }


    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
}
