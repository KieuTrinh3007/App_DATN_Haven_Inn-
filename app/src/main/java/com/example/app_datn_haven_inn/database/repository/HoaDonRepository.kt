package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.service.HoaDonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HoaDonRepository(private val api: HoaDonService) {

    // Lấy danh sách hóa đơn
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

    // Lấy danh sách phòng
    suspend fun getListPhong(): List<PhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getListPhong()
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "getListPhong Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "getListPhong Error: ${response.errorBody()}")
            null
        }
    }

    // Lấy danh sách chi tiết hóa đơn
    suspend fun getListChiTietHoaDon(): List<ChiTietHoaDonModel>? = withContext(Dispatchers.IO) {
        val response = api.getListChiTietHoaDon()
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "getListChiTietHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "getListChiTietHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    // Thêm hóa đơn
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

    // Cập nhật hóa đơn
    suspend fun updateHoaDon(id: String, carrier: HoaDonModel): HoaDonModel? = withContext(Dispatchers.IO) {
        val response = api.updateHoaDon(id, carrier)
        if (response.isSuccessful) {
            Log.d("HoaDonRepository", "updateHoaDon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("HoaDonRepository", "updateHoaDon Error: ${response.errorBody()}")
            null
        }
    }

    // Xóa hóa đơn
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
