package com.example.app_datn_haven_inn.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmThucModel(
    @SerializedName("_id") var id: String,
    @SerializedName("tenNhaHang") var tenNhaHang: String,
    @SerializedName("hinhAnh") var hinhAnh: String,
    @SerializedName("hinhAnhID") var hinhAnhID: String,
    @SerializedName("moTa") var moTa: String,
    @SerializedName("menu") var menu: ArrayList<String>,
    @SerializedName("menuIDs") var menuIDs: ArrayList<String>,
    @SerializedName("gioMoCua") var gioMoCua: String,
    @SerializedName("gioDongCua") var gioDongCua: String,
    @SerializedName("hotline") var hotline: String,
    @SerializedName("viTri") var viTri: String
) : Parcelable

