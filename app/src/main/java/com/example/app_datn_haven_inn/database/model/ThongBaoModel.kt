package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class ThongBaoModel (
//    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("tieuDe") var tieuDe: String,
    @field:SerializedName("noiDung") var noiDung: String,
    @field:SerializedName("ngayGui") var ngayGui: String,
    @field:SerializedName("trangThai") var trangThai: Boolean,


    )