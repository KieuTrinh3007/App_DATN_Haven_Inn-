package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.DichVuModel
import com.example.app_datn_haven_inn.database.model.DichVuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DichVuService {
    @GET("dichvus/")
    suspend fun getListDichVu(): Response<DichVuResponse>

    @POST("dichvus/post")
    suspend fun addDichVu(@Body DichVu: DichVuModel): Response<DichVuModel>

    @PUT("dichvus/put/{id}")
    suspend fun updateDichVu(
        @Path("id") id: String,
        @Body DichVu: DichVuModel
    ): Response<DichVuModel>

    @DELETE("dichvus/delete/{id}")
    suspend fun deleteDichVu(@Path("id") id: String): Response<Unit>
}