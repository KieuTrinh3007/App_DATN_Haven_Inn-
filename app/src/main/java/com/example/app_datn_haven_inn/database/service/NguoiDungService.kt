package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NguoiDungService {

    @GET("nguoidungs/")
    suspend fun getListNguoiDung(): Response<List<NguoiDungModel>>

    @GET("nguoiDung/{id}")
    suspend fun getNguoiDungById(@Path("id") id: String): NguoiDungModel

    @Multipart
    @PUT("nguoidungs/put/{id}")
    suspend fun updateNguoiDung(
        @Path("id") id: String,
        @Part("tenNguoiDung") tenNguoiDung: RequestBody,
        @Part("soDienThoai") soDienThoai: RequestBody,
        @Part("matKhau") matKhau: RequestBody,
        @Part("email") email: RequestBody,
        @Part("chucVu") chucVu: RequestBody,
        @Part("trangThai") trangThai: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<NguoiDungModel>

    @POST("auth/login")
    suspend fun loginNguoiDung(
        @Query("soDienThoai") soDienThoai: String,
        @Query("matKhau") matKhau: String
    ): Response<Map<String, Any>>

    // API gửi OTP
    @POST("auth/sendotp")
    suspend fun sendOtp(
        @Query("email") email: String
    ): Response<Map<String, String>>

    @POST("auth/verifyotp")
    suspend fun verifyOtp(
        @Body payload: Map<String, String>
    ): Response<Map<String, Any>>

}
