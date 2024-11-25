package com.example.app_datn_haven_inn.database.repository

import android.util.Log
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NguoiDungRepository(private val api: NguoiDungService) {

    suspend fun getListNguoiDung(): List<NguoiDungModel>? = withContext(Dispatchers.IO) {
        val response = api.getListNguoiDung()
        if (response.isSuccessful) {
            Log.d("NguoiDungRepository", "getListNguoiDung Success: ${response.body()}")
            response.body()
        } else {
            Log.e("NguoiDungRepository", "getListNguoiDung Error: ${response.errorBody()}")
            null
        }
    }

    suspend fun updateNguoiDung(id: String, carrier: NguoiDungModel,image: File): NguoiDungModel? = withContext(
        Dispatchers.IO
    ) {
        try {
            val tenNguoiDung = carrier.tenNguoiDung.toRequestBody()
            val soDienThoai = carrier.soDienThoai.toRequestBody()
            val matKhau = carrier.matKhau.toRequestBody()
            val email = carrier.email.toRequestBody()
            val chucVu = carrier.chucVu.toString().toRequestBody()
            val trangThai = carrier.trangThai.toString().toRequestBody()
            val imageRequestBody = image.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData("hinhAnh", image.name, imageRequestBody)
            val response = api.updateNguoiDung(
                id,
                tenNguoiDung,
                soDienThoai,
                matKhau,
                email,
                chucVu,
                trangThai,
                imagePart)
            if (response.isSuccessful) {
                Log.d("NguoiDungRepository", "updateNguoiDung Success: ${response.body()}")
                response.body()
            } else {
                Log.e("NguoiDungRepository", "updateNguoiDung Error: ${response.errorBody()}")
                null
            }
        }catch (e: Exception){
            Log.e("NguoiDungRepository", "addNguoiDung Error: ${e.message}")
            null
        }
    }


//    suspend fun loginNguoiDung(phone: String, password: String): String? = withContext(Dispatchers.IO) {
//        try {
//            val response = api.loginNguoiDung(phone, password)
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                val status = responseBody?.get("status") as? Double
//                if (status == 200.0) {
//                    Log.d("NguoiDungRepository", "Login success: ${responseBody["userId"]}")
//                    responseBody["userId"] as String
//                } else {
//                    Log.e("NguoiDungRepository", "Login error: ${responseBody?.get("msg")}")
//                    null
//                }
//            } else {
//                Log.e("NguoiDungRepository", "API error: ${response.errorBody()}")
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("NguoiDungRepository", "Exception: ${e.message}")
//            null
//        }
//    }

}