package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class TienNghiPhong(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("tenTienNghi") var tenTienNghi: String,
    @field:SerializedName("image") var image: String,
)

data class TienNghiPhongChiTietModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("id_TienNghi") var id_TienNghi: TienNghiPhong,
    @field:SerializedName("moTa") var moTa: String,
)

data class TienNghiPhongModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("id_TienNghi") var id_TienNghi: String,
    @field:SerializedName("moTa") var moTa: String,
    )