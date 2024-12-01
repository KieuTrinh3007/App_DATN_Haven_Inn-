package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.DanhGiaNguoiDungModel
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DanhGiaRepository (private val api: DanhGiaService) {
    suspend fun getListDanhGia(): List<DanhGiaModel>? = withContext(Dispatchers.IO) {
        val response = api.getListDanhGia()
        if (response.isSuccessful) {
            Log.d("DanhGiaRepository", "getListDanhGia Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DanhGiaRepository", "getListDanhGia Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun getListDanhGiaByIdLoaiPhong(idLoaiPhong: String): List<DanhGiaNguoiDungModel>? = withContext(Dispatchers.IO) {
        val response = api.getListDanhGiaByIdLoaiPhong(idLoaiPhong)
        if (response.isSuccessful) {
            Log.d("DanhGiaRepository", "getListDanhGia Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DanhGiaRepository", "getListDanhGia Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addDanhGia(carrier: DanhGiaModel): DanhGiaModel? = withContext(Dispatchers.IO) {
        val response = api.addDanhGia(carrier)
        if (response.isSuccessful) {
            Log.d("DanhGiaRepository", "addDanhGia Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DanhGiaRepository", "addDanhGia Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateDanhGia(id: String, carrier: DanhGiaModel): DanhGiaModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateDanhGia(id, carrier)
        if (response.isSuccessful) {
            Log.d("DanhGiaRepository", "updateDanhGia Success: ${response.body()}")
            response.body()
        } else {
            Log.e("DanhGiaRepository", "updateDanhGia Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteDanhGia(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteDanhGia(id)
        if (response.isSuccessful) {
            Log.d("DanhGiaRepository", "deleteDanhGia Success")
            true
        } else {
            Log.e("DanhGiaRepository", "deleteDanhGia Error: ${response.errorBody()}")
            false
        }
    }
}