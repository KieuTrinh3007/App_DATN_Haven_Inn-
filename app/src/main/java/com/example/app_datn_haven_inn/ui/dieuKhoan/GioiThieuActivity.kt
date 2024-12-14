package com.example.app_datn_haven_inn.ui.dieuKhoan

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_datn_haven_inn.R

class GioiThieuActivity : AppCompatActivity() {
	private lateinit var img_back : LinearLayout;
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_gioi_thieu)
		img_back = findViewById(R.id.img_back);
		
		img_back.setOnClickListener {
			onBackPressed()
		}
	}
}