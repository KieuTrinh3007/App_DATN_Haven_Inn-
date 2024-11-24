package com.example.app_datn_haven_inn.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NguoiDungModel(
    @field:SerializedName("_id") var id: String? = null,
    @field:SerializedName("tenNguoiDung") var tenNguoiDung: String,
    @field:SerializedName("soDienThoai") var soDienThoai: String,
    @field:SerializedName("matKhau") var matKhau: String,
    @field:SerializedName("email") var email: String,
    @field:SerializedName("hinhAnh") var hinhAnh: String?,
    @field:SerializedName("hinhAnhID") var hinhAnhID: String,
    @field:SerializedName("chucVu") var chucVu: Int,
    @field:SerializedName("trangThai") var trangThai: Boolean
) : Parcelable
