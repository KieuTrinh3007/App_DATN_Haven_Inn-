package com.example.app_datn_haven_inn.ui.main

import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

        val mainAdapter = MainAdapter(this)
        binding.viewPager.adapter = mainAdapter
        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        setDefault()
                        binding.ivHome.setColorFilter(getColor(R.color.white))
                    }

                    1 -> {
                        setDefault()
                        binding.ivFavorite.setColorFilter(getColor(R.color.white))
                    }

                    2 -> {
                        setDefault()
                        binding.ivNotification.setColorFilter(getColor(R.color.white))
                    }

                    3 -> {
                        setDefault()
                        binding.ivProfile.setColorFilter(getColor(R.color.white))
                    }
                }
            }
        })

        binding.llHome.setOnClickListener() {
            binding.viewPager.currentItem = 0
        }
        binding.llFavorite.setOnClickListener {
            binding.viewPager.currentItem = 1
        }
        binding.llNotification.setOnClickListener {
            binding.viewPager.currentItem = 2
        }
        binding.llProfile.setOnClickListener {
            binding.viewPager.currentItem = 3
        }
    }

    private fun setDefault() {
        binding.ivHome.setColorFilter(getColor(R.color.black))
        binding.ivFavorite.setColorFilter(getColor(R.color.black))
        binding.ivNotification.setColorFilter(getColor(R.color.black))
        binding.ivProfile.setColorFilter(getColor(R.color.black))
    }
}

