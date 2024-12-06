package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class HoaDonModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("id_NguoiDung") var id_NguoiDung: String,
    @field:SerializedName("id_Coupon") var id_Coupon: String,
    @field:SerializedName("ngayNhanPhong") var ngayNhanPhong: String,
    @field:SerializedName("ngayTraPhong") var ngayTraPhong: String,
    @field:SerializedName("soLuongKhach") var soLuongKhach: Int,
    @field:SerializedName("soLuongPhong") var soLuongPhong: Int,
    @field:SerializedName("ngayThanhToan") var ngayThanhToan: String,
    @field:SerializedName("phuongThucThanhToan") var phuongThucThanhToan: String,
    @field:SerializedName("trangThai") var trangThai: Int,
    @field:SerializedName("ghiChu") var ghiChu: String,
    @field:SerializedName("tongTien") var tongTien: Double,
    @field:SerializedName("phong") var phong: PhongModel? = null // Đảm bảo có trường phong


    )