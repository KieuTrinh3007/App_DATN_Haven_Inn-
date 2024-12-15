package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel1
import com.example.app_datn_haven_inn.database.model.Phong1Model
import com.example.app_datn_haven_inn.database.model.PhongModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HoaDonService {
    @GET("hoadons/")
    suspend fun getListHoaDon(): Response<List<HoaDonModel>>

    @GET("phongs/")
    suspend fun getListPhong(): Response<List<PhongModel>>

    @PUT("hoadons/{id}")
    suspend fun updateHoaDon(
        @Path("id") id: String,
        @Body hoaDon: HoaDonModel
    ): Response<HoaDonModel>

    @GET("chitiethoadons/")
    suspend fun getListChiTietHoaDon(): Response<List<ChiTietHoaDonModel>>

    @POST("hoadons/post")
    suspend fun addHoaDon(@Body HoaDon: HoaDonModel): Response<HoaDonModel>

    @PUT("hoadons/huy/{id}")
    suspend fun huyHoaDon(
        @Path("id") id: String
    ): Response<HoaDonModel>

    @DELETE("hoadons/delete/{id}")
    suspend fun deleteHoaDon(@Path("id") id: String): Response<Unit>

    @GET("phong/{id}")
    suspend fun getPhongDetails(@Path("id") id: String): Response<Phong1Model>

    @GET("hoadons/history")
    suspend fun getHistory(
        @Query("id_NguoiDung") idNguoiDung: String,
        @Query("trangThai") trangThai: Int?
    ): Response<List<HoaDonModel1>>
}