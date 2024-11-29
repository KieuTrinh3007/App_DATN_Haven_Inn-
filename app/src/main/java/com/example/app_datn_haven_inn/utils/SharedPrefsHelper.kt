package com.example.app_datn_haven_inn.utils

import android.content.Context

object SharedPrefsHelper {
    fun getIdNguoiDung(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("idNguoiDung", null)
    }
}