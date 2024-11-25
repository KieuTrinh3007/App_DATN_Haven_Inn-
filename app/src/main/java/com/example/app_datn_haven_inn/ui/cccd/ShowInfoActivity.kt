package com.example.app_datn_haven_inn.ui.cccd

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R

class ShowInfoActivity : AppCompatActivity() {

    private lateinit var imageViewFront: ImageView
    private lateinit var imageViewBack: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewGender: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewNationality: TextView
    private lateinit var textViewCCCD: TextView
    private lateinit var textViewDateCap: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)

        // Ánh xạ các View
        imageViewFront = findViewById(R.id.imageViewFrontCCCD)
        imageViewBack = findViewById(R.id.imageViewBackCCCD)
        textViewName = findViewById(R.id.textViewNameCCCD)
        textViewGender = findViewById(R.id.textViewGenderCCCD)
        textViewBirthDate = findViewById(R.id.textViewBirthDateCCCD)
        textViewAddress = findViewById(R.id.textViewAddressCCCD)
        textViewNationality = findViewById(R.id.textViewNationalityCCCD)
        textViewCCCD = findViewById(R.id.textNumberCCCD)
        textViewDateCap = findViewById(R.id.textDateCapCCCD)

        // Lấy đường dẫn ảnh từ Intent
        val frontImagePath = intent.getStringExtra("frontImagePath")
        val backImagePath = intent.getStringExtra("backImagePath")

        // Hiển thị ảnh mặt trước
        frontImagePath?.let {
            val frontBitmap = BitmapFactory.decodeFile(it)
            frontBitmap?.let { bitmap ->
                imageViewFront.setImageBitmap(bitmap)
                extractCCCDData(bitmap, isFront = true)
            } ?: showToast("Không thể tải ảnh mặt trước!")
        }

        // Hiển thị ảnh mặt sau
        backImagePath?.let {
            val backBitmap = BitmapFactory.decodeFile(it)
            backBitmap?.let { bitmap ->
                imageViewBack.setImageBitmap(bitmap)
                extractCCCDData(bitmap, isFront = false)
            } ?: showToast("Không thể tải ảnh mặt sau!")
        }
    }

    private fun extractCCCDData(bitmap: Bitmap, isFront: Boolean) {
        val extractor = CCCDDataExtractor()
        extractor.extractCCCDInfo(bitmap) { dataMap ->
            if (dataMap.isNotEmpty()) {
                if (isFront) {
                    // Cập nhật thông tin mặt trước
                    textViewCCCD.text = "Số CCCD: ${dataMap["cccd_number"] ?: "Không rõ"}"
                    textViewName.text = "Họ và tên: ${dataMap["name"] ?: "Không rõ"}"
                    textViewGender.text = "Giới tính: ${dataMap["gender"] ?: "Không rõ"}"
                    textViewBirthDate.text = "Ngày sinh: ${dataMap["birth_date"] ?: "Không rõ"}"
                    textViewAddress.text = "Nơi cư trú: ${dataMap["address"] ?: "Không rõ"}"
                    textViewNationality.text = "Quốc tịch: ${dataMap["nationality"] ?: "Không rõ"}"
                } else {
                    // Cập nhật thông tin mặt sau
                    textViewDateCap.text = "Ngày cấp: ${dataMap["issue_date"] ?: "Không rõ"}"
                }
            } else {
                showToast(if (isFront) "Không thể nhận diện thông tin mặt trước!" else "Không thể nhận diện thông tin mặt sau!")
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
