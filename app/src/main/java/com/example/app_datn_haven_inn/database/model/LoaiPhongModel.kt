package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class LoaiPhongModel(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("tenLoaiPhong") var tenLoaiPhong: String,
    @field:SerializedName("giuong") var giuong: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("dienTich") var dienTich: Double,
    @field:SerializedName("hinhAnh") var hinhAnh: ArrayList<String>,
    @field:SerializedName("hinhAnhIDs") var hinhAnhIDs: ArrayList<String>,
    @field:SerializedName("giaTien") var giaTien: Double,
    @field:SerializedName("moTa") var moTa: String,
    @field:SerializedName("trangThai") var trangThai: Boolean,
)