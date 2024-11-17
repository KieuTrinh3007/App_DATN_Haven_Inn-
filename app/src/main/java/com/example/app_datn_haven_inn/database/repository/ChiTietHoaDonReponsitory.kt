package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.service.ChiTietHoaDonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChiTietHoaDonReponsitory (private val api: ChiTietHoaDonService) {
    
    suspend fun getListChiTietHoaDon(): List<ChiTietHoaDonModel>? = withContext(Dispatchers.IO) {
        val response = api.getListChiTietHoaDon()
        if (response.isSuccessful) {
            Log.d("ChiTietHoaDonReponsitory", "getListChiTietHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ChiTietHoaDonReponsitory", "getListChiTietHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addChiTietHoaDon(carrier: ChiTietHoaDonModel): ChiTietHoaDonModel? = withContext(Dispatchers.IO) {
        val response = api.addChiTietHoaDon(carrier)
        if (response.isSuccessful) {
            Log.d("ChiTietHoaDonReponsitory", "addChiTietHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ChiTietHoaDonReponsitory", "addChiTietHoaDon Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateChiTietHoaDon(id: String, carrier: ChiTietHoaDonModel): ChiTietHoaDonModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateChiTietHoaDon(id, carrier)
        if (response.isSuccessful) {
            Log.d("ChiTietHoaDonReponsitory", "updateChiTietHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ChiTietHoaDonReponsitory", "updateChiTietHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteChiTietHoaDon(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteChiTietHoaDon(id)
        if (response.isSuccessful) {
            Log.d("ChiTietHoaDonReponsitory", "deleteChiTietHoaDon Success")
            true
        } else {
            Log.e("ChiTietHoaDonReponsitory", "deleteChiTietHoaDon Error: ${response.errorBody()}")
            false
        }
    }
}