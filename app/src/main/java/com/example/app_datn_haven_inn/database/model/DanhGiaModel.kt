package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName



data class DanhGiaNguoiDungModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("soDiem") var soDiem: Double,
    @field:SerializedName("binhLuan") var binhLuan: String,
    @field:SerializedName("createdAt") var ngayDanhGia: String,
    @field:SerializedName("hinhAnh") var hinhAnh: String,
    @field:SerializedName("tenNguoiDung") var tenNguoiDung: String,
)

data class DanhGiaModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("soDiem") var soDiem: Double,
    @field:SerializedName("binhLuan") var binhLuan: String,
    @field:SerializedName("createdAt") var ngayDanhGia: String
    )