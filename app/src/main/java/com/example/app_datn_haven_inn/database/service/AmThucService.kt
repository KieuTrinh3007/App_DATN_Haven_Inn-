package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.AmThucModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AmThucService {
    @GET("amthucs/")
    suspend fun getListAmThuc(): Response<List<AmThucModel>>

    @POST("amthucs/post")
    suspend fun addAmThuc(@Body AmThuc: AmThucModel): Response<AmThucModel>

    @PUT("amthucs/put/{id}")
    suspend fun updateAmThuc(
        @Path("id") id: String,
        @Body AmThuc: AmThucModel
    ): Response<AmThucModel>

    @DELETE("amthucs/delete/{id}")
    suspend fun deleteAmThuc(@Path("id") id: String): Response<Unit>
}