package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class AmThucModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("tenNhaHang") var tenNhaHang: String,
    @field:SerializedName("hinhAnh") var hinhAnh: String,
    @field:SerializedName("hinhAnhID") var hinhAnhID: String,
    @field:SerializedName("moTa") var moTa: String,
    @field:SerializedName("menu") var menu: ArrayList<String>,
    @field:SerializedName("menuIDs") var menuIDs: ArrayList<String>,
    @field:SerializedName("gioMoCua") var gioMoCua: String,
    @field:SerializedName("gioDongCua") var gioDongCua: String,
    @field:SerializedName("hotline") var hotline: String,
    @field:SerializedName("viTri") var viTri: String,
)