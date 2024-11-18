package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.service.HoaDonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HoaDonRepository (private val api: HoaDonService) {
    suspend fun getListHoaDon(): List<HoaDonModel>? = withContext(Dispatchers.IO) {
        val response = api.getListHoaDon()
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "getListHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "getListHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addHoaDon(carrier: HoaDonModel): HoaDonModel? = withContext(Dispatchers.IO) {
        val response = api.addHoaDon(carrier)
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "addHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "addHoaDon Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateHoaDon(id: String, carrier: HoaDonModel): HoaDonModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateHoaDon(id, carrier)
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "updateHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "updateHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteHoaDon(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteHoaDon(id)
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "deleteHoaDon Success")
            true
        } else {
            Log.e("HoaDonRepository", "deleteHoaDon Error: ${response.errorBody()}")
            false
        }
    }
    
}