package com.example.app_datn_haven_inn.ui.cccd

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R
import com.google.android.material.textfield.TextInputEditText
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


        val frontImagePath = intent.getStringExtra("frontImagePath")
        val backImagePath = intent.getStringExtra("backImagePath")
        val cccd = intent.getStringExtra("cccd")
        val name = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val birthDate = intent.getStringExtra("birthDate")
        val address = intent.getStringExtra("address")
        val issueDate = intent.getStringExtra("issueDate")

        disableEditText(textViewName)
        disableEditText(textViewGender)
        disableEditText(textViewBirthDate)
        disableEditText(textViewAddress)
        disableEditText(textViewCCCD)
        disableEditText(textViewDateCap)
        // Hiển thị ảnh mặt trước
//        if (!frontImagePath.isNullOrEmpty()) {
//            val frontBitmap = BitmapFactory.decodeFile(frontImagePath)
//            imageViewFront.setImageBitmap(frontBitmap)
//        } else {
//            showToast("Không tìm thấy ảnh mặt trước")
//        }
//
//        // Hiển thị ảnh mặt sau
//        if (!backImagePath.isNullOrEmpty()) {
//            val backBitmap = BitmapFactory.decodeFile(backImagePath)
//            imageViewBack.setImageBitmap(backBitmap)
//        } else {
//            showToast("Không tìm thấy ảnh mặt sau")
//        }

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
                dateString // Nếu không thể chuyển đổi, trả về ngày gốc
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dateString // Trả về ngày gốc nếu có lỗi
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
