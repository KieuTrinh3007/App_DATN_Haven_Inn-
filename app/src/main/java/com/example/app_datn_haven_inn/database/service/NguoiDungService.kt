package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface NguoiDungService {
    @GET("nguoidungs/")
    suspend fun getListNguoiDung(): Response<List<NguoiDungModel>>

    @Multipart
    @POST("nguoidungs/post")
    suspend fun addNguoiDung(
                                 @Part("tenNguoiDung")  tenNguoiDung: RequestBody,
                                 @Part("soDienThoai")  soDienThoai: RequestBody,
                                 @Part("matKhau")  matKhau: RequestBody,
                                 @Part("email")  email: RequestBody,
                                 @Part("chucVu")  chucVu: RequestBody,
                                 @Part("trangThai")  trangThai: RequestBody,
                                 @Part("hinhAnh") image: MultipartBody.Part,): Response<NguoiDungModel>
            
    @Multipart
    @PUT("nguoidungs/put/{id}")
    suspend fun updateNguoiDung(
        @Path("id") id: String,
        @Part("tenNguoiDung")  tenNguoiDung: RequestBody,
        @Part("soDienThoai")  soDienThoai: RequestBody,
        @Part("matKhau")  matKhau: RequestBody,
        @Part("email")  email: RequestBody,
        @Part("chucVu")  chucVu: RequestBody,
        @Part("trangThai")  trangThai: RequestBody,
        @Part("hinhAnh") image: MultipartBody.Part
    ): Response<NguoiDungModel>


    @PUT("nguoidungs/:actions/{id}")
    suspend fun updateAction(
        @Path("id") id: String,
        @Body NguoiDung: NguoiDungModel,
    ): Response<NguoiDungModel>

    @DELETE("nguoidungs/delete/{id}")
    suspend fun deleteNguoiDung(@Path("id") id: String): Response<Unit>


}