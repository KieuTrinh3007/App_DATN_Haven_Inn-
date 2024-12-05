package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.PhongModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PhongService {
    @GET("phongs/")
    suspend fun getListPhong(): Response<List<PhongModel>>

    @GET("phongs/")
    suspend fun getListPhongByIdLoaiPhong(@Query("id_LoaiPhong") idLoaiPhong: String): Response<List<PhongModel>>

    @POST("phongs/post")
    suspend fun addPhong(@Body Phong: PhongModel): Response<PhongModel>

    @PUT("phongs/put/{id}")
    suspend fun updatePhong(
        @Path("id") id: String,
        @Body Phong: PhongModel
    ): Response<PhongModel>

    @DELETE("phongs/delete/{id}")
    suspend fun deletePhong(@Path("id") id: String): Response<Unit>

    @GET("api/nguoidungs/myroom/{userId}")
    fun getUserRooms(@Path("userId") userId: String): Call<List<PhongModel>>
}