package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.TienNghiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TienNghiService {
    @GET("tiennghis/")
    suspend fun getListTienNghi(): Response<List<TienNghiModel>>

    @POST("tiennghis/post")
    suspend fun addTienNghi(@Body TienNghi: TienNghiModel): Response<TienNghiModel>

    @PUT("tiennghis/put/{id}")
    suspend fun updateTienNghi(
        @Path("id") id: String,
        @Body TienNghi: TienNghiModel
    ): Response<TienNghiModel>

    @DELETE("tiennghis/delete/{id}")
    suspend fun deleteTienNghi(@Path("id") id: String): Response<Unit>
}