package com.example.app_datn_haven_inn.ui.home

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.PhongNghi
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentHomeBinding
import com.example.app_datn_haven_inn.ui.home.Faragment.ServiceFragment
import com.example.app_datn_haven_inn.ui.home.Fragment.OverviewFragment
import com.example.app_datn_haven_inn.ui.profile.ProfileFragment
import com.example.app_datn_haven_inn.ui.home.adapter.CategoryAdapter
import com.example.app_datn_haven_inn.ui.home.adapter.SlideshowAdapter
import com.example.app_datn_haven_inn.ui.thucDon.ThucDonFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val handler = Handler(Looper.getMainLooper())
    private val images = listOf(
        R.drawable.slideshow1,
        R.drawable.slideshow1,
        R.drawable.slideshow1
    )

    private val categories = listOf(
        "Tổng quan", "Tiện nghi, dịch vụ", "Phòng", "Ẩm thực"
    )

    override fun inflateViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        // Setup slideshow
        val slideshowAdapter = SlideshowAdapter(images)
        viewBinding.viewPager.adapter = slideshowAdapter

        // Setup categories RecyclerView
        val categoryAdapter = CategoryAdapter(categories) { position ->
            navigateToChildFragment(position)
        }
        viewBinding.rvCategory.adapter = categoryAdapter

        // Handle slideshow auto-scroll
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

        // Update dots for the slideshow
        updateDots(0)

        // Default fragment display
        navigateToChildFragment(0)
    }

    private fun updateDots(position: Int) {
        val dotViews = listOf(
            viewBinding.ivDot1,
            viewBinding.ivDot2,
            viewBinding.ivDot3
        )
        dotViews.forEachIndexed { index, dotView ->
            dotView.setBackgroundResource(
                if (index == position) R.drawable.iv_dot_on else R.drawable.iv_dot_off
            )
        }
    }

    private fun navigateToChildFragment(position: Int) {
        val fragment = when (position) {
            0 -> OverviewFragment()
            1 -> ServiceFragment()
            2 -> PhongNghi() // Replace with the actual "RoomFragment"
            3 -> ThucDonFragment() // Replace with the actual "DiningFragment"
            else -> OverviewFragment()
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.childFragmentContainer, fragment)
            .commit()
    }
}
