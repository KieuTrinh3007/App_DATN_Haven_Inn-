package com.example.app_datn_haven_inn.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChiTietHoaDonModel(
    @field:SerializedName("id_Phong") var id_Phong: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("giaPhong") var giaPhong: Double,
    @field:SerializedName("buaSang") var buaSang: Boolean,
) : Parcelable

@Parcelize
data class ChiTietHoaDonModel1(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_Phong") var id_Phong: Phong1Model,
    @field:SerializedName("id_HoaDon") var id_HoaDon: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("giaPhong") var giaPhong: Double,
    @field:SerializedName("buaSang") var buaSang: Boolean,
    @field:SerializedName("tongTien") var tongTien: Double,
    @field:SerializedName("tenLoaiPhong") var tenLoaiPhong: String? = null,
    @field:SerializedName("soPhong") var soPhong: String? = null,
    @field:SerializedName("hinhAnh") var hinhAnh: List<String> = emptyList()
) : Parcelable

