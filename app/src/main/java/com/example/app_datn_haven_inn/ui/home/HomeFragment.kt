package com.example.app_datn_haven_inn.ui.home

import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var viewPager: ViewPager2
    private lateinit var circleIndicator: CircleIndicator3
    private lateinit var binding: FragmentHomeBinding

    override fun inflateViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding = inflateViewBinding()
        super.initView()
    }
}
