package com.example.app_datn_haven_inn.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.app_datn_haven_inn.ui.favorite.FavoriteFragment
import com.example.app_datn_haven_inn.ui.home.HomeFragment
import com.example.app_datn_haven_inn.ui.notification.NotificationFragment
import com.example.app_datn_haven_inn.ui.profile.ProfileFragment
import com.example.app_datn_haven_inn.ui.room.fragment.PhongNghiFragment

class MainAdapter(fragmentManager: FragmentActivity, private val idNguoiDung: String?) : FragmentStateAdapter(fragmentManager) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            2 -> PhongNghiFragment()
            3 -> NotificationFragment()
            4 -> {
                // Truyền idNguoiDung vào ProfileFragment
                val profileFragment = ProfileFragment()
                val bundle = Bundle()
                bundle.putString("idNguoiDung", idNguoiDung)
                profileFragment.arguments = bundle
                profileFragment
            }
            else -> HomeFragment()
        }
    }
}
