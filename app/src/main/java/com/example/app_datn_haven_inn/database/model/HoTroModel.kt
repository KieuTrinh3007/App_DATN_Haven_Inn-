package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class HoTroModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("vanDe") var vanDe: String,
    @field:SerializedName("trangThai") var trangThai: Int,


    )