package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.TienNghiModel
import com.example.app_datn_haven_inn.database.model.TienNghiPhongChiTietModel
import com.example.app_datn_haven_inn.database.model.TienNghiPhongModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TienNghiPhongService {
    @GET("tiennghiphongs/")
    suspend fun getListTienNghiPhong(): Response<List<TienNghiPhongModel>>

    @GET("tiennghiphongs/")
    suspend fun getListTienNghiByIdLoaiPhong(@Query("id_LoaiPhong") idLoaiPhong: String): Response<List<TienNghiPhongChiTietModel>>

    @POST("tiennghiphongs/post")
    suspend fun addTienNghiPhong(@Body TienNghiPhong: TienNghiPhongModel): Response<TienNghiPhongModel>

    @PUT("tiennghiphongs/put/{id}")
    suspend fun updateTienNghiPhong(
        @Path("id") id: String,
        @Body TienNghiPhong: TienNghiPhongModel
    ): Response<TienNghiPhongModel>

    @DELETE("tiennghiphongs/delete/{id}")
    suspend fun deleteTienNghiPhong(@Path("id") id: String): Response<Unit>
}