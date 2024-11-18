package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChiTietHoaDonService {
    @GET("chitiethoadons/")
    suspend fun getListChiTietHoaDon(): Response<List<ChiTietHoaDonModel>>

    @POST("chitiethoadons/post")
    suspend fun addChiTietHoaDon(@Body ChiTietHoaDon: ChiTietHoaDonModel): Response<ChiTietHoaDonModel>

    @PUT("chitiethoadons/put/{id}")
    suspend fun updateChiTietHoaDon(
        @Path("id") id: String,
        @Body ChiTietHoaDon: ChiTietHoaDonModel
    ): Response<ChiTietHoaDonModel>

    @DELETE("chitiethoadons/delete/{id}")
    suspend fun deleteChiTietHoaDon(@Path("id") id: String): Response<Unit>
}