package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LoginService {
    @GET("login/")
    suspend fun getListNguoiDung(): Response<List<NguoiDungModel>>

    @POST("login/post")
    suspend fun addNguoiDung(@Body NguoiDung: NguoiDungModel): Response<NguoiDungModel>

    @PUT("login/doimatkhau/{id}")
    suspend fun updateNguoiDung(
        @Path("id") id: String,
        @Body NguoiDung: NguoiDungModel
    ): Response<NguoiDungModel>


}