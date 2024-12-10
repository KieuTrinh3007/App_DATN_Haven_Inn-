package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class CouponModel (
    @field:SerializedName("_id") var id: String?= null,
    @field:SerializedName("id_Coupon") var id_cp: String?= null,
    @field:SerializedName("maGiamGia") var maGiamGia: String,
    @field:SerializedName("giamGia") var giamGia: Double,
    @field:SerializedName("giamGiaToiDa") var giamGiaToiDa: Double,
    @field:SerializedName("dieuKienToiThieu") var dieuKienToiThieu: Int,
    @field:SerializedName("ngayBatDau") var ngayBatDau: String,
    @field:SerializedName("ngayHetHan") var ngayKetThuc: String,
    @field:SerializedName("trangThai") var soLanSuDung: Int,
    @field:SerializedName("id_Coupon") var id_cp: String?= null,
    )