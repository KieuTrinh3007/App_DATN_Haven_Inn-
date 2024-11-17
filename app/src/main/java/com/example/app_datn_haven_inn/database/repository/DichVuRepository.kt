package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.DichVuModel
import com.example.app_datn_haven_inn.database.service.DichVuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DichVuRepository (private val api: DichVuService) {
    suspend fun getListDichVu(): List<DichVuModel>? = withContext(Dispatchers.IO) {
        val response = api.getListDichVu()
        if (response.isSuccessful) {
            Log.d("DichVuRepository", "getListDichVu Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DichVuRepository", "getListDichVu Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addDichVu(carrier: DichVuModel): DichVuModel? = withContext(Dispatchers.IO) {
        val response = api.addDichVu(carrier)
        if (response.isSuccessful) {
            Log.d("DichVuRepository", "addDichVu Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DichVuRepository", "addDichVu Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateDichVu(id: String, carrier: DichVuModel): DichVuModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateDichVu(id, carrier)
        if (response.isSuccessful) {
            Log.d("DichVuRepository", "updateDichVu Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DichVuRepository", "updateDichVu Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteDichVu(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteDichVu(id)
        if (response.isSuccessful) {
            Log.d("DichVuRepository", "deleteDichVu Success")
            true
        } else {
            Log.e("DichVuRepository", "deleteDichVu Error: ${response.errorBody()}")
            false
        }
    }
}