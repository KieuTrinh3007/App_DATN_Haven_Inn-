package com.example.app_datn_haven_inn.ui.main

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()

    fun showBottomBar(isVisible: Boolean) {
        binding.viewPager2.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lấy idNguoiDung từ SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idNguoiDung = sharedPreferences.getString("idNguoiDung", null)

        // Truyền idNguoiDung vào MainAdapter
        val mainAdapter = MainAdapter(this, idNguoiDung)
        binding.viewPager2.adapter = mainAdapter
        binding.viewPager2.offscreenPageLimit = 5
        binding.viewPager2.isUserInputEnabled = false

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> setDefaultIcons().also { binding.ivHome.setColorFilter(getColor(R.color.white)) }
                    1 -> setDefaultIcons().also { binding.ivFavorite.setColorFilter(getColor(R.color.white)) }
                    2 -> setDefaultIcons().also { binding.ivDatPhong.setColorFilter(getColor(R.color.white)) }
                    3 -> setDefaultIcons().also { binding.ivNotification.setColorFilter(getColor(R.color.white)) }
                    4 -> setDefaultIcons().also { binding.ivProfile.setColorFilter(getColor(R.color.white)) }
                }
            }
        })

        binding.llHome.setOnClickListener { binding.viewPager2.currentItem = 0 }
        binding.llFavorite.setOnClickListener { binding.viewPager2.currentItem = 1 }
        binding.llDatPhong.setOnClickListener { binding.viewPager2.currentItem = 2 }
        binding.llNotification.setOnClickListener { binding.viewPager2.currentItem = 3 }
        binding.llProfile.setOnClickListener { binding.viewPager2.currentItem = 4 }
    }

    private fun setDefaultIcons() {
        binding.ivHome.setColorFilter(getColor(R.color.black))
        binding.ivFavorite.setColorFilter(getColor(R.color.black))
        binding.ivDatPhong.setColorFilter(getColor(R.color.black))
        binding.ivNotification.setColorFilter(getColor(R.color.black))
        binding.ivProfile.setColorFilter(getColor(R.color.black))
    }
}
