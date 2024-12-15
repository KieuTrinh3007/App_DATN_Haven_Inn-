package com.example.app_datn_haven_inn.ui.dieuKhoan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityDieuKhoan1Binding
import com.example.app_datn_haven_inn.databinding.ActivityDieuKhoan2Binding
import com.example.app_datn_haven_inn.databinding.ActivityDieuKhoan3Binding
import com.example.app_datn_haven_inn.databinding.ActivityDieuKhoan4Binding

class dieuKhoan : AppCompatActivity() {
    private lateinit var img_back : LinearLayout;
    private lateinit var txtDieuKhoan1 : TextView;
    private lateinit var txtDieuKhoan2 : TextView
    private lateinit var txtdieuKhoan3 : TextView
    private lateinit var txtDieuKhoan4 : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dieu_khoan)
        txtDieuKhoan1 = findViewById(R.id.txtdieuKhoan1)
        txtDieuKhoan2 = findViewById(R.id.txtdieuKhoan2)
        txtdieuKhoan3 = findViewById(R.id.txtdieuKhoan3)
        txtDieuKhoan4 = findViewById(R.id.txtdieuKhoan4)
        img_back = findViewById(R.id.img_back);
        
        img_back.setOnClickListener {
            onBackPressed()
        }


        txtDieuKhoan1.setOnClickListener {
            val intent = Intent(this, dieuKhoan1::class.java)
            startActivity(intent)
        }
        txtDieuKhoan2.setOnClickListener {
            val intent = Intent(this, dieuKhoan2::class.java)
            startActivity(intent)
        }
        txtdieuKhoan3.setOnClickListener {
            val intent = Intent(this, dieuKhoan3::class.java)
            startActivity(intent)
        }
        txtDieuKhoan4.setOnClickListener {
            val intent = Intent(this, dieuKhoan4::class.java)
            startActivity(intent)
        }

    }
}