package com.example.app_datn_haven_inn.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null
    fun init(context: Context?) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
    fun saveFavoriteState(context: Context, phong: LoaiPhongModel) {
        val sharedPref = context.getSharedPreferences("YeuThichPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(phong.id.toString(), phong.isFavorite ?: false)  // Lưu trạng thái yêu thích theo ID phòng
        editor.apply()
    }

    fun loadFavoriteState(context: Context, phong: LoaiPhongModel): Boolean {
        val sharedPref = context.getSharedPreferences("YeuThichPref", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(phong.id.toString(), false)  // Lấy trạng thái yêu thích từ SharedPreferences
    }

    fun getId(context: Context) :String {
        val sharedPref = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        return sharedPref.getString("id", "") ?: ""
    }

    fun setId(context: Context, id: String) {
        val sharedPref = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("id", id)
        editor.apply()

    }
}