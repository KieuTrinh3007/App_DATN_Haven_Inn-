package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.FavoriteRequest
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import com.example.app_datn_haven_inn.database.service.YeuThichService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YeuThichRepository (private val api: YeuThichService) {
    suspend fun getListYeuThich(): List<YeuThichModel>? = withContext(Dispatchers.IO) {
        val response = api.getListYeuThich()
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "getListYeuThich Success: ${response.body()}")
            response.body()
        } else {
            Log.e("YeuThichRepository", "getListYeuThich Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun getFavoritesByUserId(idNguoiDung : String): List<LoaiPhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getFavoritesByUserId(idNguoiDung)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "getListYeuThich Success: ${response.body()}")
            response.body()
        } else {
            Log.e("YeuThichRepository", "getListYeuThich Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addYeuThich( yeuThich : FavoriteRequest): YeuThichModel? = withContext(Dispatchers.IO) {
        val response = api.addYeuThich(yeuThich)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "addYeuThich Success: ${response.body()}")
            response.body()
        } else {
            Log.e("YeuThichRepository", "addYeuThich Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateYeuThich(id: String, carrier: YeuThichModel): YeuThichModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateYeuThich(id, carrier)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "updateYeuThich Success: ${response.body()}")
            response.body()
        } else {
            Log.e("YeuThichRepository", "updateYeuThich Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteYeuThich(idLoaiPhong: String,idNguoiDung: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteYeuThich(idLoaiPhong,idNguoiDung)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "deleteYeuThich Success")
            true
        } else {
            Log.e("YeuThichRepository", "deleteYeuThich Error: ${response.errorBody()}")
            false
        }
    }


}