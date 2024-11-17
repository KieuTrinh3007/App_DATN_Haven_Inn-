package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.HoTroModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HoTroService {
    @GET("hotros/")
    suspend fun getListHoTro(): Response<List<HoTroModel>>

    @POST("hotros/post")
    suspend fun addHoTro(@Body HoTro: HoTroModel): Response<HoTroModel>

    @PUT("hotros/put/{id}")
    suspend fun updateHoTro(
        @Path("id") id: String,
        @Body HoTro: HoTroModel
    ): Response<HoTroModel>

    @DELETE("hotros/delete/{id}")
    suspend fun deleteHoTro(@Path("id") id: String): Response<Unit>
}