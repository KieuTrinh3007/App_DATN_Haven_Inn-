package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.TienNghiPhongModel
import com.example.app_datn_haven_inn.database.service.TienNghiPhongService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TienNghiPhongRepository (private val api: TienNghiPhongService) {
    suspend fun getListTienNghiPhong(): List<TienNghiPhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getListTienNghiPhong()
        if (response.isSuccessful) {
            Log.d("TienNghiPhongRepository", "getListTienNghiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiPhongRepository", "getListTienNghiPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addTienNghiPhong(carrier: TienNghiPhongModel): TienNghiPhongModel? = withContext(Dispatchers.IO) {
        val response = api.addTienNghiPhong(carrier)
        if (response.isSuccessful) {
            Log.d("TienNghiPhongRepository", "addTienNghiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiPhongRepository", "addTienNghiPhong Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateTienNghiPhong(id: String, carrier: TienNghiPhongModel): TienNghiPhongModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateTienNghiPhong(id, carrier)
        if (response.isSuccessful) {
            Log.d("TienNghiPhongRepository", "updateTienNghiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiPhongRepository", "updateTienNghiPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteTienNghiPhong(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteTienNghiPhong(id)
        if (response.isSuccessful) {
            Log.d("TienNghiPhongRepository", "deleteTienNghiPhong Success")
            true
        } else {
            Log.e("TienNghiPhongRepository", "deleteTienNghiPhong Error: ${response.errorBody()}")
            false
        }
    }
}