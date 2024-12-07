package com.example.app_datn_haven_inn.database.model

import com.google.gson.annotations.SerializedName

data class TienNghiModel (
    @field:SerializedName("_id") var id: String,
    @field:SerializedName("tenTienNghi") var tenTienNghi: String,
    @field:SerializedName("image") var image: String,
    @field:SerializedName("imageId") var imageId: String,
    )