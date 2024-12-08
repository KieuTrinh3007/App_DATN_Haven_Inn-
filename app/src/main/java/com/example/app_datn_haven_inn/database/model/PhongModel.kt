package com.example.app_datn_haven_inn.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PhongModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("soPhong") var soPhong: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: String,
    @field:SerializedName("VIP") var vip: Boolean,
    @field:SerializedName("trangThai") var trangThai: Int,
    var isSelected  : Boolean = false,
    var isBreakfast: Boolean = false,
    var loaiPhong:  @RawValue LoaiPhongModel? = null

): Parcelable

// Model cho thông tin phòng
@Parcelize
data class Phong1Model(
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("soPhong") var soPhong: String,
    @field:SerializedName("id_LoaiPhong") var id_LoaiPhong: LoaiPhongModel,
    @field:SerializedName("trangThai") var trangThai: Int,
    @field:SerializedName("VIP") var VIP: Boolean
) : Parcelable