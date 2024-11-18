package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class DanhGiaModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("soDiem") var soDiem: Double,
    @field:SerializedName("binhLuan") var binhLuan: String,


    )