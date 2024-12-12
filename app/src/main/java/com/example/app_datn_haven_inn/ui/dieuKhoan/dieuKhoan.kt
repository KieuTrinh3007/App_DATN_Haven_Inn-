package com.example.app_datn_haven_inn.ui.dieuKhoan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dieu_khoan)
        val txtDieuKhoan1: TextView = findViewById(R.id.txtdieuKhoan1)
        val txtDieuKhoan2: TextView = findViewById(R.id.txtdieuKhoan2)
        val txtdieuKhoan3: TextView = findViewById(R.id.txtdieuKhoan3)
        val txtdieuKhoan4: TextView = findViewById(R.id.txtdieuKhoan4)


        txtDieuKhoan1.setOnClickListener {
            val intent = Intent(this, ActivityDieuKhoan1Binding::class.java)
            startActivity(intent)
        }
        txtDieuKhoan2.setOnClickListener {
            val intent = Intent(this, ActivityDieuKhoan2Binding::class.java)
            startActivity(intent)
        }
        txtdieuKhoan3.setOnClickListener {
            val intent = Intent(this, ActivityDieuKhoan3Binding::class.java)
            startActivity(intent)
        }
        txtdieuKhoan4.setOnClickListener {
            val intent = Intent(this, ActivityDieuKhoan4Binding::class.java)
            startActivity(intent)
        }

    }
}