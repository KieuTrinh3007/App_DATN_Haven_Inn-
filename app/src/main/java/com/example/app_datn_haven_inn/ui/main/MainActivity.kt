package com.example.app_datn_haven_inn.ui.main

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lấy idNguoiDung từ SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idNguoiDung = sharedPreferences.getString("idNguoiDung", null)

        // Truyền idNguoiDung vào MainAdapter
        val mainAdapter = MainAdapter(this, idNguoiDung)
        binding.viewPager.adapter = mainAdapter
        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> setDefaultIcons().also { binding.ivHome.setColorFilter(getColor(R.color.white)) }
                    1 -> setDefaultIcons().also { binding.ivFavorite.setColorFilter(getColor(R.color.white)) }
                    2 -> setDefaultIcons().also { binding.ivNotification.setColorFilter(getColor(R.color.white)) }
                    3 -> setDefaultIcons().also { binding.ivProfile.setColorFilter(getColor(R.color.white)) }
                }
            }
        })

        binding.llHome.setOnClickListener { binding.viewPager.currentItem = 0 }
        binding.llFavorite.setOnClickListener { binding.viewPager.currentItem = 1 }
        binding.llNotification.setOnClickListener { binding.viewPager.currentItem = 2 }
        binding.llProfile.setOnClickListener { binding.viewPager.currentItem = 3 }
    }

    private fun setDefaultIcons() {
        binding.ivHome.setColorFilter(getColor(R.color.black))
        binding.ivFavorite.setColorFilter(getColor(R.color.black))
        binding.ivNotification.setColorFilter(getColor(R.color.black))
        binding.ivProfile.setColorFilter(getColor(R.color.black))
    }
}
