package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.HoaDonModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HoaDonService {
    @GET("hoadons/")
    suspend fun getListHoaDon(): Response<List<HoaDonModel>>

    @POST("hoadons/post")
    suspend fun addHoaDon(@Body HoaDon: HoaDonModel): Response<HoaDonModel>

    @PUT("hoadons/put/{id}")
    suspend fun updateHoaDon(
        @Path("id") id: String,
        @Body HoaDon: HoaDonModel
    ): Response<HoaDonModel>

    @DELETE("hoadons/delete/{id}")
    suspend fun deleteHoaDon(@Path("id") id: String): Response<Unit>
}