package com.example.app_datn_haven_inn.database.service

import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.model.CouponModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CouponService {
    @GET("coupons/")
    suspend fun getListCoupon(): Response<List<CouponModel>>

    @POST("coupons/post")
    suspend fun addCoupon(@Body Coupon: CouponModel): Response<CouponModel>

    @PUT("coupons/put/{id}")
    suspend fun updateCoupon(
        @Path("id") id: String,
        @Body Coupon: CouponModel
    ): Response<CouponModel>

    @DELETE("coupons/delete/{id}")
    suspend fun deleteCoupon(@Path("id") id: String): Response<Unit>
}