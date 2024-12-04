package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.service.PhongService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhongRepository (private val api: PhongService) {
    suspend fun getListPhong(): List<PhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getListPhong()
        if (response.isSuccessful) {
            Log.d("PhongRepository", "getListPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("PhongRepository", "getListPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun getListPhongByIdLoaiPhong(idLoaiPhong: String): List<PhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getListPhongByIdLoaiPhong(idLoaiPhong)
        if (response.isSuccessful) {
            Log.d("PhongRepository", "getListPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("PhongRepository", "getListPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addPhong(carrier: PhongModel): PhongModel? = withContext(Dispatchers.IO) {
        val response = api.addPhong(carrier)
        if (response.isSuccessful) {
            Log.d("PhongRepository", "addPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("PhongRepository", "addPhong Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updatePhong(id: String, carrier: PhongModel): PhongModel? = withContext(
        Dispatchers.IO) {
        val response = api.updatePhong(id, carrier)
        if (response.isSuccessful) {
            Log.d("PhongRepository", "updatePhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("PhongRepository", "updatePhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deletePhong(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deletePhong(id)
        if (response.isSuccessful) {
            Log.d("PhongRepository", "deletePhong Success")
            true
        } else {
            Log.e("PhongRepository", "deletePhong Error: ${response.errorBody()}")
            false
        }
    }
    data class RoomResponse(
        val data: List<PhongModel>
    )
}