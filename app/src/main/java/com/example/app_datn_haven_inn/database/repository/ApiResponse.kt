package com.example.app_datn_haven_inn.database.repository

import com.example.app_datn_haven_inn.database.model.PhongModel

data class ApiResponse(
    val message: String?,  // Thông báo lỗi khi không có phòng
    val data: List<PhongModel>?
)

