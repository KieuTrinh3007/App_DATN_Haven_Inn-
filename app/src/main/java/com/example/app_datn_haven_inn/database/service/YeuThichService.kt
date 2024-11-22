package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface YeuThichService {
    @GET("yeuthichs/")
    suspend fun getListYeuThich(): Response<List<YeuThichModel>>

    @GET("yeuthichs/")
    suspend fun getFavoritesByUserId(@Query("id_NguoiDung") userId: String): Response<List<LoaiPhongModel>>

    @POST("yeuthichs/post")
    suspend fun addYeuThich(@Body YeuThich: YeuThichModel): Response<YeuThichModel>

    @PUT("yeuthichs/put/{id}")
    suspend fun updateYeuThich(
        @Path("id") id: String,
        @Body YeuThich: YeuThichModel
    ): Response<YeuThichModel>

    @DELETE("yeuthichs/delete/{id_LoaiPhong}/{id_NguoiDung}")
    suspend fun deleteYeuThich(@Path("id_LoaiPhong") idLoaiPhong: String,@Path("id_NguoiDung") idNguoiDung: String): Response<Unit>
}