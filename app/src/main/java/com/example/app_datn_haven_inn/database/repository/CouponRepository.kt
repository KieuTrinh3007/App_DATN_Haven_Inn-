package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.CouponModel
import com.example.app_datn_haven_inn.database.service.CouponService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CouponRepository (private val api: CouponService) {
    suspend fun getListCoupon(): List<CouponModel>? = withContext(Dispatchers.IO) {
        val response = api.getListCoupon()
        if (response.isSuccessful) {
            Log.d("CouponRepository", "getListCoupon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("CouponRepository", "getListCoupon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addCoupon(carrier: CouponModel): CouponModel? = withContext(Dispatchers.IO) {
        val response = api.addCoupon(carrier)
        if (response.isSuccessful) {
            Log.d("CouponRepository", "addCoupon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("CouponRepository", "addCoupon Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateCoupon(id: String, carrier: CouponModel): CouponModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateCoupon(id, carrier)
        if (response.isSuccessful) {
            Log.d("CouponRepository", "updateCoupon Success: ${response.body()}")
            response.body()
        } else {
            Log.e("CouponRepository", "updateCoupon Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun deleteCoupon(id: String): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteCoupon(id)
        if (response.isSuccessful) {
            Log.d("CouponRepository", "deleteCoupon Success")
            true
        } else {
            Log.e("CouponRepository", "deleteCoupon Error: ${response.errorBody()}")
            false
        }
    }
    suspend fun getListCouponByUser(idNguoiDung: String): List<CouponModel>? = withContext(Dispatchers.IO) {
        val response = api.getListCouponByUser(idNguoiDung)
        if (response.isSuccessful) {
            Log.d("CouponRepository", "getListCouponByUser Success: ${response.body()}")
            response.body()
        } else {
            Log.e("CouponRepository", "getListCouponByUser Error: ${response.errorBody()}")
            null
        }
    }

}