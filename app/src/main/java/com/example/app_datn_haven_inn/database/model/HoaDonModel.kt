package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class HoaDonModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("id_Coupon") var id_Coupon: String,
    @field:SerializedName("ngayNhanPhong") var ngayNhanPhong: String,
    @field:SerializedName("ngayTraPhong") var ngayTraPhong: String,
    @field:SerializedName("tongPhong") var tongPhong: Int,
    @field:SerializedName("tongKhach") var tongKhach: Int,
    @field:SerializedName("tongTien") var tongTien: Double,
    @field:SerializedName("ngayThanhToan") var ngayThanhToan: String,
    @field:SerializedName("trangThai") var trangThai: Int,
    @field:SerializedName("ghiChu") var ghiChu: String,
    @field:SerializedName("giamGia") var giamGia: Int,



    )