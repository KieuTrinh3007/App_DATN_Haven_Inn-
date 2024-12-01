package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.service.LoaiPhongService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoaiPhongRepository (private val api: LoaiPhongService) {
    suspend fun getListLoaiPhong(): List<LoaiPhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getListLoaiPhong()
        if (response.isSuccessful) {
            Log.d("LoaiPhongRepository", "getListLoaiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoaiPhongRepository", "getListLoaiPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun getLoaiPhongById(id: String):LoaiPhongModel? = withContext(Dispatchers.IO) {
        val response = api.getLoaiPhongById(id)
        if (response.isSuccessful) {
            Log.d("CarrierRepository", "getCarrierById Success: ${response.body()}")
            response.body()
        } else {
            Log.e("CarrierRepository", "getCarrierById Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addLoaiPhong(carrier: LoaiPhongModel): LoaiPhongModel? = withContext(Dispatchers.IO) {
        val response = api.addLoaiPhong(carrier)
        if (response.isSuccessful) {
            Log.d("LoaiPhongRepository", "addLoaiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoaiPhongRepository", "addLoaiPhong Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateLoaiPhong(id: String, carrier: LoaiPhongModel): LoaiPhongModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateLoaiPhong(id, carrier)
        if (response.isSuccessful) {
            Log.d("LoaiPhongRepository", "updateLoaiPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoaiPhongRepository", "updateLoaiPhong Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteLoaiPhong(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteLoaiPhong(id)
        if (response.isSuccessful) {
            Log.d("LoaiPhongRepository", "deleteLoaiPhong Success")
            true
        } else {
            Log.e("LoaiPhongRepository", "deleteLoaiPhong Error: ${response.errorBody()}")
            false
        }
    }
}