package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class ChiTietHoaDonModel (
    @field:SerializedName("id_Phong") var id_Phong: String,
    @field:SerializedName("id_HoaDon") var id_HoaDon: String? = null,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("giaPhong") var giaPhong: Double,
    @field:SerializedName("buaSang") var buaSang: Boolean,
    )

data class ChiTietHoaDonModel1 (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_Phong") var id_Phong: Phong1Model,
    @field:SerializedName("id_HoaDon") var id_HoaDon: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("giaPhong") var giaPhong: Double,
    @field:SerializedName("buaSang") var buaSang: Boolean,
    )