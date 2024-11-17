package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ThongBaoService {
    @GET("thongbaos/")
    suspend fun getListThongBao(): Response<List<ThongBaoModel>>

    @POST("thongbaos/post")
    suspend fun addThongBao(@Body ThongBao: ThongBaoModel): Response<ThongBaoModel>

    @PUT("thongbaos/put/{id}")
    suspend fun updateThongBao(
        @Path("id") id: String,
        @Body ThongBao: ThongBaoModel
    ): Response<ThongBaoModel>

    @DELETE("thongbaos/delete/{id}")
    suspend fun deleteThongBao(@Path("id") id: String): Response<Unit>
}