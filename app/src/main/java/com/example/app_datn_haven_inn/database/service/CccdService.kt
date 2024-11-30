package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.CccdModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CccdService {
    // Phương thức GET: Lấy thông tin CCCD theo userId
    @GET("cccd/getcccd/{id}")
    suspend fun getCccdByUserId(
        @Path("userId") userId: String // Tham số userId trong URL
    ): Response<CccdModel>

    @Multipart
    @POST("cccd/post")
    suspend fun addCccd(
        @Part("id_NguoiDung") idNguoiDung: RequestBody,
        @Part("soCCCD") soCCCD: RequestBody,
        @Part("hoTen") hoTen: RequestBody,
        @Part("ngaySinh") ngaySinh: RequestBody,
        @Part("gioiTinh") gioiTinh: RequestBody,
        @Part("ngayCap") ngayCap: RequestBody,
        @Part("noiThuongTru") queQuan: RequestBody,
        @Part matTruoc: MultipartBody.Part?,
        @Part matSau: MultipartBody.Part?
    ): Response<Map<String, Any>>
}
