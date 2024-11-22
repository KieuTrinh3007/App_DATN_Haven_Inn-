package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LoaiPhongService {
    @GET("loaiphongs/")
    suspend fun getListLoaiPhong(): Response<List<LoaiPhongModel>>

    @GET("loaiphongs/getListorByID/{id}")
    suspend fun getLoaiPhongById(@Path("id") id: String): Response<LoaiPhongModel>

    @POST("loaiphongs/post")
    suspend fun addLoaiPhong(@Body LoaiPhong: LoaiPhongModel): Response<LoaiPhongModel>

    @PUT("loaiphongs/put/{id}")
    suspend fun updateLoaiPhong(
        @Path("id") id: String,
        @Body LoaiPhong: LoaiPhongModel
    ): Response<LoaiPhongModel>

    @DELETE("loaiphongs/delete/{id}")
    suspend fun deleteLoaiPhong(@Path("id") id: String): Response<Unit>
}