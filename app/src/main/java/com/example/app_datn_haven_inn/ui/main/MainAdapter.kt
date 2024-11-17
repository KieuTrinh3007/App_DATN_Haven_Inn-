package com.example.app_datn_haven_inn.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.app_datn_haven_inn.ui.booking.fragment.BookingFragment
import com.example.app_datn_haven_inn.ui.favorite.FavoriteFragment
import com.example.app_datn_haven_inn.ui.home.HomeFragment
import com.example.app_datn_haven_inn.ui.profile.ProfileFragment
import com.example.app_datn_haven_inn.ui.room.fragment.RoomDetailFragment

class MainAdapter (fragmentManager: FragmentActivity) : FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            2 -> RoomDetailFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()

        }
    }
}