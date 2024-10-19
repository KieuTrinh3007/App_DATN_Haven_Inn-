package com.example.app_datn_haven_inn.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.app_datn_haven_inn.ui.favorite.FavoriteFragment
import com.example.app_datn_haven_inn.ui.home.HomeFragment
import com.example.app_datn_haven_inn.ui.notification.NotificationFragment
import com.example.app_datn_haven_inn.ui.profile.ProfileFragment

class MainAdapter (fragmentManager: FragmentActivity) : FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            2 -> NotificationFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()

        }
    }
}