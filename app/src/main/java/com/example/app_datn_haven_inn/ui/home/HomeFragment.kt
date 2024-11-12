package com.example.app_datn_haven_inn.ui.home

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentHomeBinding
import com.example.app_datn_haven_inn.ui.home.adapter.CategoryAdapter
import com.example.app_datn_haven_inn.ui.home.adapter.SlideshowAdapter


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    val handler = Handler(Looper.getMainLooper())
    val images =
        listOf(
            R.drawable.slideshow1,
            R.drawable.slideshow1,
            R.drawable.slideshow1
        )

    val categories = listOf (
        "Tổng quan","Tiện nghi, dịch vụ","Phòng","Ẩm thực"

    )

    override fun inflateViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        val slideshowAdapter = SlideshowAdapter(images)
        viewBinding.viewPager.adapter = slideshowAdapter

        val categoryAdapter = CategoryAdapter(categories)
        viewBinding.rvCategory.adapter = categoryAdapter

        viewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        val runnable = object : Runnable {
            var currentPage = 0
            override fun run() {
                if (currentPage == images.size) {
                    currentPage = 0
                }
                viewBinding.viewPager.setCurrentItem(currentPage, true)
                updateDots(currentPage)
                currentPage++
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)

        updateDots(0)

    }

    private fun updateDots(position: Int) {
        val dotViews = listOf(
            viewBinding.ivDot1,
            viewBinding.ivDot2,
            viewBinding.ivDot3,

            )

        dotViews.forEachIndexed { index, dotView ->
            dotView.setBackgroundResource(
                if (index == position) R.drawable.iv_dot_on else R.drawable.iv_dot_off
            )
        }
    }
}