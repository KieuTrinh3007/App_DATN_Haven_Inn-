package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.service.AmThucService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AmThucRepository (private val api: AmThucService) {
    suspend fun getListAmThuc(): List<AmThucModel>? = withContext(Dispatchers.IO) {
        val response = api.getListAmThuc()
        if (response.isSuccessful) {
            Log.d("AmThucRepository", "getListAmThuc Success: ${response.body()}")
            response.body()
        } else {
            Log.e("AmThucRepository", "getListAmThuc Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addAmThuc(carrier: AmThucModel): AmThucModel? = withContext(Dispatchers.IO) {
        val response = api.addAmThuc(carrier)
        if (response.isSuccessful) {
            Log.d("AmThucRepository", "addAmThuc Success: ${response.body()}")
            response.body()
        } else {
            Log.e("AmThucRepository", "addAmThuc Error: ${response.errorBody()}")
            null
        }
    }
    

    suspend fun updateAmThuc(id: String, carrier: AmThucModel): AmThucModel? = withContext(Dispatchers.IO) {
        val response = api.updateAmThuc(id, carrier)
        if (response.isSuccessful) {
            Log.d("AmThucRepository", "updateAmThuc Success: ${response.body()}")
            response.body()
        } else {
            Log.e("AmThucRepository", "updateAmThuc Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteAmThuc(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteAmThuc(id)
        if (response.isSuccessful) {
            Log.d("AmThucRepository", "deleteAmThuc Success")
            true
        } else {
            Log.e("AmThucRepository", "deleteAmThuc Error: ${response.errorBody()}")
            false
        }
    }

}