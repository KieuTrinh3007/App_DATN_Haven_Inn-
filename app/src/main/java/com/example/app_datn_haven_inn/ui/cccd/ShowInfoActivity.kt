package com.example.app_datn_haven_inn.ui.cccd

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.CccdService
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ShowInfoActivity : AppCompatActivity() {

    private lateinit var textViewName: TextInputEditText
    private lateinit var textViewGender: TextInputEditText
    private lateinit var textViewBirthDate: TextInputEditText
    private lateinit var textViewAddress: TextInputEditText
    private lateinit var textViewCCCD: TextInputEditText
    private lateinit var textViewDateCap: TextInputEditText
    private lateinit var btnVerify: TextView
    private lateinit var img_back_vemt: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)

        // Ánh xạ các View
        textViewName = findViewById(R.id.textViewNameCCCD)
        textViewGender = findViewById(R.id.textViewGenderCCCD)
        textViewBirthDate = findViewById(R.id.textViewBirthDateCCCD)
        textViewAddress = findViewById(R.id.textViewAddressCCCD)
        textViewCCCD = findViewById(R.id.textNumberCCCD)
        textViewDateCap = findViewById(R.id.textDateCapCCCD)
        btnVerify = findViewById(R.id.buttonAddCCCD)
        img_back_vemt = findViewById(R.id.img_back_vemt)

        val frontImagePath = intent.getStringExtra("frontImagePath")
        val backImagePath = intent.getStringExtra("backImagePath")
        val cccd = intent.getStringExtra("cccd")
        val name = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val birthDate = intent.getStringExtra("birthDate")
        val address = intent.getStringExtra("address")
        val issueDate = intent.getStringExtra("issueDate")
        val idNguoiDung = intent.getStringExtra("idNguoiDung")
        Log.d("idNguoiDung1", "onCreate: " + idNguoiDung)

        disableEditText(textViewName)
        disableEditText(textViewGender)
        disableEditText(textViewBirthDate)
        disableEditText(textViewAddress)
        disableEditText(textViewCCCD)
        disableEditText(textViewDateCap)

        // Hiển thị thông tin CCCD
        if (!cccd.isNullOrEmpty() && !name.isNullOrEmpty() && !gender.isNullOrEmpty()) {
            textViewCCCD.setText(cccd)
            textViewName.setText(name)
            textViewGender.setText(gender)
            textViewBirthDate.setText(formatDate("$birthDate"))
            textViewAddress.setText(address)
            textViewDateCap.setText(formatDate("$issueDate"))
        } else {
            showToast("Không có thông tin CCCD để hiển thị")
        }

        Log.d(
            "showinfo",
            "idNguoiDung: $idNguoiDung, frontImagePath: $frontImagePath, backImagePath: $backImagePath"
        )

        btnVerify.setOnClickListener {
            if (idNguoiDung != null && frontImagePath != null && backImagePath != null) {
                uploadCccdToServer(
                    idNguoiDung,
                    cccd ?: "",
                    name ?: "",
                    gender ?: "",
                    formatDate("$birthDate") ?: "",
                    address ?: "",
                    formatDate("$issueDate") ?: "",
                    frontImagePath,
                    backImagePath,
                )
            } else {
                showToast("Dữ liệu không đầy đủ để xác thực")
            }


        }

        img_back_vemt.setOnClickListener {
            val intent = Intent(this, CaptureFrontActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadCccdToServer(
        userId: String,
        cccd: String,
        name: String,
        gender: String,
        birthDate: String,
        address: String,
        issueDate: String,
        frontImagePath: String,
        backImagePath: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Chuẩn bị dữ liệu
                val service = CreateService.createService<CccdService>()
                val idNguoiDung = createRequestBody(userId)
                val soCCCD = createRequestBody(cccd)
                val hoTen = createRequestBody(name)
                val gioiTinh = createRequestBody(gender)
                val ngaySinh = createRequestBody(birthDate)
                val queQuan = createRequestBody(address)
                val ngayCap = createRequestBody(issueDate)
                val matTruocPart = createMultipartBody("matTruoc", File(frontImagePath))
                val matSauPart = createMultipartBody("matSau", File(backImagePath))

                // Gửi yêu cầu lên server
                val response = service.addCccd(
                    idNguoiDung, soCCCD, hoTen, ngaySinh, gioiTinh, ngayCap, queQuan,
                    matTruocPart, matSauPart
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        showToast("Xác thực thành công!")
                    } else {
                        showToast("Lỗi xác thực: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Lỗi: ${e.message}")
                }
            }
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private fun createMultipartBody(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun disableEditText(editText: TextInputEditText) {
        editText.isFocusable = false
        editText.isClickable = false
    }

    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
