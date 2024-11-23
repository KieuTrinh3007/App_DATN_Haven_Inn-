package com.example.app_datn_haven_inn.database

import com.example.app_datn_haven_inn.utils.Constans

object CreateService {
    inline fun <reified T> createService(): T {
        return NetworkModule<T>(Constans.DOMAIN).create(T::class.java)
    }
}

