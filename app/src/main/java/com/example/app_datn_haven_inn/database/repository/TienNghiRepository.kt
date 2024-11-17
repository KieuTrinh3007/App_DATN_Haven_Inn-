package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.TienNghiModel
import com.example.app_datn_haven_inn.database.service.TienNghiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TienNghiRepository (private val api: TienNghiService) {
    suspend fun getListTienNghi(): List<TienNghiModel>? = withContext(Dispatchers.IO) {
        val response = api.getListTienNghi()
        if (response.isSuccessful) {
            Log.d("TienNghiRepository", "getListTienNghi Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiRepository", "getListTienNghi Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addTienNghi(carrier: TienNghiModel): TienNghiModel? = withContext(Dispatchers.IO) {
        val response = api.addTienNghi(carrier)
        if (response.isSuccessful) {
            Log.d("TienNghiRepository", "addTienNghi Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiRepository", "addTienNghi Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateTienNghi(id: String, carrier: TienNghiModel): TienNghiModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateTienNghi(id, carrier)
        if (response.isSuccessful) {
            Log.d("TienNghiRepository", "updateTienNghi Success: ${response.body()}")
            response.body()
        } else {
            Log.e("TienNghiRepository", "updateTienNghi Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteTienNghi(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteTienNghi(id)
        if (response.isSuccessful) {
            Log.d("TienNghiRepository", "deleteTienNghi Success")
            true
        } else {
            Log.e("TienNghiRepository", "deleteTienNghi Error: ${response.errorBody()}")
            false
        }
    }
}