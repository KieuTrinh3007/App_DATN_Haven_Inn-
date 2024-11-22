package com.example.app_datn_haven_inn.database.repository

import android.util.Log
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

    suspend fun getFavoritesByUserId(idUser : String): List<LoaiPhongModel>? = withContext(Dispatchers.IO) {
        val response = api.getFavoritesByUserId(idUser)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "getListYeuThich Success: ${response.body()}")
            response.body()
        } else {
            Log.e("YeuThichRepository", "getListYeuThich Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addYeuThich(carrier: YeuThichModel): YeuThichModel? = withContext(Dispatchers.IO) {
        val response = api.addYeuThich(carrier)
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

    suspend fun deleteYeuThich(idLoaiPhong: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteYeuThich(idLoaiPhong)
        if (response.isSuccessful) {
            Log.d("YeuThichRepository", "deleteYeuThich Success")
            true
        } else {
            Log.e("YeuThichRepository", "deleteYeuThich Error: ${response.errorBody()}")
            false
        }
    }


}