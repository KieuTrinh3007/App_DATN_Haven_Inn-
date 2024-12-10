package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.model.CccdModel
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.repository.ApiResponse
import com.example.app_datn_haven_inn.database.repository.PhongRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NguoiDungService {

    @GET("nguoidungs/")
    suspend fun getListNguoiDung(): Response<List<NguoiDungModel>>

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
    suspend fun login(
        @Query("email") email: String,
        @Query("matKhau") matKhau: String,
        @Query("deviceToken") deviceToken: String
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

    @POST("auth/register")
    suspend fun registerNguoiDung(
        @Body nguoiDung: NguoiDungModel
    ): Response<Map<String, String>>

    @PUT("auth/doimatkhau/{id}")
    suspend fun setupPassword(
        @Body payload: Map<String, String>
    ): Response<Map<String, String>>

    @POST("auth/forgotpass")
    suspend fun forgotPass(
        @Body payload: Map<String, String>
    ): Response<Map<String, String>>

    @POST("auth/setuppass")
    suspend fun setUpPass(
        @Body payload: Map<String, String>
    ): Response<Map<String, String>>

    @GET("loaiphong/{id}")
    suspend fun getLoaiPhongById(@Path("id") id: String): Response<LoaiPhongModel>

    @GET("nguoidungs/myroom/{id}")
    suspend fun myRoom(@Path("id") id: String): Response<List<ChiTietHoaDonModel1>>


}
