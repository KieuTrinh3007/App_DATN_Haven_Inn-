package com.example.app_datn_haven_inn.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment <FragmentHomeBinding>() {
    override fun inflateViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()


    }


}