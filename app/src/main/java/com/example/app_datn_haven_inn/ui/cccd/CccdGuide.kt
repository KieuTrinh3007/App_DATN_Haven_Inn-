package com.example.app_datn_haven_inn.ui.cccd

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.main.MainActivity

class CccdGuide : AppCompatActivity() {
    private lateinit var btn_bat_dau_chup: TextView
    private lateinit var btn_back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cccd_guide)

        btn_bat_dau_chup = findViewById(R.id.btn_bat_dau_chup)
        btn_back = findViewById(R.id.img_back_xt1)

        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToFragment", 4) // 4 là vị trí của ProfileFragment
            startActivity(intent)
            finish() // Kết thúc Activity hiện tại
        }


        val idNguoiDung = intent.getStringExtra("idNguoiDung")

        btn_bat_dau_chup.setOnClickListener {
            val intent = Intent(this, CaptureFrontActivity::class.java)
            intent.putExtra("idNguoiDung", idNguoiDung)
            startActivity(intent)
        }
    }
}