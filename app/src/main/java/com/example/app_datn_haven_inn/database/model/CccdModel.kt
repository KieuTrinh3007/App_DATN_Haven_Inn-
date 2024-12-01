package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class CccdModel(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("id_NguoiDung") var nguoiDung: String? = null,
    @SerializedName("soCCCD") var soCCCD: String? = null,
    @SerializedName("hoTen") var hoTen: String? = null,
    @SerializedName("ngaySinh") var ngaySinh: String? = null,
    @SerializedName("gioiTinh") var gioiTinh: String? = null,
    @SerializedName("ngayCap") var ngayCap: String? = null,
    @SerializedName("noiThuongTru") var noiThuongTru: String? = null,
    @SerializedName("anhMatTruoc") var anhMatTruoc: String? = null,
    @SerializedName("anhMatTruocId") var anhMatTruocId: String? = null,
    @SerializedName("anhMatSau") var anhMatSau: String? = null,
    @SerializedName("anhMatSauId") var anhMatSauId: String? = null
)
