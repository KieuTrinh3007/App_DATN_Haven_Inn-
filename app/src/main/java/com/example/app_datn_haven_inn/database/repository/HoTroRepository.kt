package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.HoTroModel
import com.example.app_datn_haven_inn.database.service.HoTroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HoTroRepository (private val api: HoTroService) {
    suspend fun getListHoTro(): List<HoTroModel>? = withContext(Dispatchers.IO) {
        val response = api.getListHoTro()
        if (response.isSuccessful) {
            Log.d("HoTroRepository", "getListHoTro Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoTroRepository", "getListHoTro Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addHoTro(carrier: HoTroModel): HoTroModel? = withContext(Dispatchers.IO) {
        val response = api.addHoTro(carrier)
        if (response.isSuccessful) {
            Log.d("HoTroRepository", "addHoTro Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoTroRepository", "addHoTro Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateHoTro(id: String, carrier: HoTroModel): HoTroModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateHoTro(id, carrier)
        if (response.isSuccessful) {
            Log.d("HoTroRepository", "updateHoTro Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoTroRepository", "updateHoTro Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteHoTro(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteHoTro(id)
        if (response.isSuccessful) {
            Log.d("HoTroRepository", "deleteHoTro Success")
            true
        } else {
            Log.e("HoTroRepository", "deleteHoTro Error: ${response.errorBody()}")
            false
        }
    }
}