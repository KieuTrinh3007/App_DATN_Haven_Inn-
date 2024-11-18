package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.database.service.ThongBaoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThongBaoRepository (private val api: ThongBaoService)  {

    suspend fun getListThongBao(): List<ThongBaoModel>? = withContext(Dispatchers.IO) {
        val response = api.getListThongBao()
        if (response.isSuccessful) {
            Log.d("ThongBaoRepository", "getListThongBao Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ThongBaoRepository", "getListThongBao Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addThongBao(carrier: ThongBaoModel): ThongBaoModel? = withContext(Dispatchers.IO) {
        val response = api.addThongBao(carrier)
        if (response.isSuccessful) {
            Log.d("ThongBaoRepository", "addThongBao Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ThongBaoRepository", "addThongBao Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateThongBao(id: String, carrier: ThongBaoModel): ThongBaoModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateThongBao(id, carrier)
        if (response.isSuccessful) {
            Log.d("ThongBaoRepository", "updateThongBao Success: ${response.body()}")
            response.body()
        } else {
            Log.e("ThongBaoRepository", "updateThongBao Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteThongBao(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteThongBao(id)
        if (response.isSuccessful) {
            Log.d("ThongBaoRepository", "deleteThongBao Success")
            true
        } else {
            Log.e("ThongBaoRepository", "deleteThongBao Error: ${response.errorBody()}")
            false
        }
    }
}