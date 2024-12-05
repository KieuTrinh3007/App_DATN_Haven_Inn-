package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class PhongModel(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("soPhong") var soPhong: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("VIP") var vip: Boolean,
    @field:SerializedName("trangThai") var trangThai: Int,
    // Thêm loaiPhong nếu cần thiết
    var loaiPhong: LoaiPhongModel? = null,
    var isSelected  : Boolean = false
)

// Model cho thông tin phòng
data class Phong1Model(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("soPhong") var soPhong: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: LoaiPhongModel,  // Đối tượng LoaiPhongModel
    @field:SerializedName("trangThai") var trangThai: Int,
    @field:SerializedName("VIP") var VIP: Boolean
)