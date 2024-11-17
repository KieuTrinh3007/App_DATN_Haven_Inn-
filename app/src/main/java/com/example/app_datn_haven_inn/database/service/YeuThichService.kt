package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.YeuThichModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface YeuThichService {
    @GET("YeuThichs/")
    suspend fun getListYeuThich(): Response<List<YeuThichModel>>

    @POST("YeuThichs/post")
    suspend fun addYeuThich(@Body YeuThich: YeuThichModel): Response<YeuThichModel>

    @PUT("YeuThichs/put/{id}")
    suspend fun updateYeuThich(
        @Path("id") id: String,
        @Body YeuThich: YeuThichModel
    ): Response<YeuThichModel>

    @DELETE("YeuThichs/delete/{id}")
    suspend fun deleteYeuThich(@Path("id") id: String): Response<Unit>
}