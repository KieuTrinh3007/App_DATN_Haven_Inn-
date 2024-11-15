package com.example.app_datn_haven_inn.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentRoomDetailBinding

class SignIn : AppCompatActivity() {

	private var _binding: FragmentRoomDetailBinding? = null
	private val binding get() = _binding!!

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_login)

	}


}