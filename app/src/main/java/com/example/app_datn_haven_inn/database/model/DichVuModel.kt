package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName
data class DichVuResponse(

    @field:SerializedName("status") var status: String,
    @field:SerializedName("data") var data: List<DichVuModel>
        )

data class DichVuModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("tenDichVu") var tenDichVu: String,
    @field:SerializedName("hinhAnh") var hinhAnh: String,
    @field:SerializedName("hinhAnhID") var hinhAnhID: String,
    @field:SerializedName("moTa") var moTa: String,

    )