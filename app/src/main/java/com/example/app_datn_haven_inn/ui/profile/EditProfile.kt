package com.example.app_datn_haven_inn.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.File

class EditProfile : AppCompatActivity() {
    private lateinit var nguoiDungService: NguoiDungService
    private var idNguoiDung: String? = null

    private lateinit var imageViewAvatar: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewEmail: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var btSaveChanges: TextView

    private val IMAGE_PICK_CODE = 1000
    private var selectedImageFilePath: String? = null

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

        // Set listener for save button
        btSaveChanges.setOnClickListener {
            updateUserInfo()
        }

        // Set listener to change profile image
        imageViewAvatar.setOnClickListener {
            openGalleryForImage()
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
                    // Prepare image file part
                    val imagePart = prepareImageFilePart(selectedImageFilePath)

                    val response = nguoiDungService.updateNguoiDung(
                        id = id,
                        tenNguoiDung = name.toRequestBody(),
                        soDienThoai = "".toRequestBody(), // Keep phone number unchanged
                        matKhau = "".toRequestBody(), // Keep password unchanged
                        email = "".toRequestBody(), // Keep email unchanged
                        chucVu = "".toRequestBody(), // Keep position unchanged
                        trangThai = "true".toRequestBody(),
                        image = imagePart // Pass the image or null
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
            return null // If no image is selected, return null to keep the current image
        }
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("hinhAnh", file.name, requestFile)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data
            imageUri?.let {
                selectedImageFilePath = getFilePathFromUri(it)
                Glide.with(this).load(it).into(imageViewAvatar)
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath
    }

    private fun String.toRequestBody(): RequestBody = this.toRequestBody()
}
