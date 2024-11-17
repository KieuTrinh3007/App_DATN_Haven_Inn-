package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository (private val api: LoginService) {
    suspend fun getListNguoiDung(): List<NguoiDungModel>? = withContext(Dispatchers.IO) {
        val response = api.getListNguoiDung()
        if (response.isSuccessful) {
            Log.d("LoginRepository", "getListNguoiDung Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoginRepository", "getListNguoiDung Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun addNguoiDung(carrier: NguoiDungModel): NguoiDungModel? = withContext(Dispatchers.IO) {
        val response = api.addNguoiDung(carrier)
        if (response.isSuccessful) {
            Log.d("LoginRepository", "addNguoiDung Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoginRepository", "addNguoiDung Error: ${response.errorBody()}")
            null
        }
    }


    suspend fun updateNguoiDung(id: String, carrier: NguoiDungModel): NguoiDungModel? = withContext(
        Dispatchers.IO) {
        val response = api.updateNguoiDung(id, carrier)
        if (response.isSuccessful) {
            Log.d("LoginRepository", "updateNguoiDung Success: ${response.body()}")
            response.body()
        } else {
            Log.e("LoginRepository", "updateNguoiDung Error: ${response.errorBody()}")
            null
        }
    }

    }
