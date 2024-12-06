package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class ChiTietHoaDonModel(
    @field:SerializedName("_id") var id: String? = null,  // Giá trị mặc định là null
    @field:SerializedName("id_Phong") var id_Phong: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("giaPhong") var giaPhong: Double,
    @field:SerializedName("buaSang") var buaSang: Boolean
)
