package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.DanhGiaNguoiDungModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DanhGiaService {
    @GET("danhgias/")
    suspend fun getListDanhGia(): Response<List<DanhGiaModel>>

    @GET("danhgias/")
    suspend fun getListDanhGiaByIdLoaiPhong(@Query("id_LoaiPhong") idLoaiPhong: String): Response<List<DanhGiaNguoiDungModel>>

    @GET("danhgias/")
    suspend fun getListDanhGiaByIdUser(@Query("id_NguoiDung") idNguoiDung: String): Response<List<DanhGiaNguoiDungModel>>

//    @GET("danhgias/{id}")
//    suspend fun getDanhGiaByUser(@Query("id_NguoiDung") idNguoiDung: String): Response<List<DanhGiaNguoiDungModel>>

    @POST("danhgias/post")
    suspend fun addDanhGia(@Body DanhGia: DanhGiaModel): Response<DanhGiaModel>

    @PUT("danhgias/put/{id}")
    suspend fun updateDanhGia(
        @Path("id") id: String,
        @Body DanhGia: Double
    ): Response<DanhGiaModel>

    @DELETE("danhgias/delete/{id}")
    suspend fun deleteDanhGia(@Path("id") id: String): Response<Unit>
}