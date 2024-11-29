package com.example.app_datn_haven_inn.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.service.DichVuService
import com.example.app_datn_haven_inn.database.service.TienNghiService
import com.example.app_datn_haven_inn.ui.home.Faragment.ServiceAdapter
import com.example.app_datn_haven_inn.ui.home.adapter.ServicesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceFragment : AppCompatActivity() {
    // Khai báo các thành phần UI
    private lateinit var recyclerViewService: RecyclerView
    private lateinit var recyclerViewServices: RecyclerView

    // Khai báo các adapter
    private lateinit var adapter: ServiceAdapter
    private lateinit var adapterServices: ServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_service)

        // Ánh xạ các thành phần UI
        recyclerViewService = findViewById(R.id.recyclerViewService)
        recyclerViewServices = findViewById(R.id.recyclerViewServices)

        // Thiết lập adapter và layout cho RecyclerView đầu tiên
        adapter = ServiceAdapter()
        recyclerViewService.layoutManager = GridLayoutManager(this, 2)
        recyclerViewService.adapter = adapter

        // Thiết lập adapter và layout cho RecyclerView thứ hai
        adapterServices = ServicesAdapter()
        recyclerViewServices.layoutManager = LinearLayoutManager(this)
        recyclerViewServices.adapter = adapterServices

        // Gọi API để lấy dữ liệu
        fetchServices()
        fetchServices2()
    }

    private fun fetchServices() {
        lifecycleScope.launch {
            try {
                val service = CreateService.createService<TienNghiService>()
                val response = withContext(Dispatchers.IO) { service.getListTienNghi() }

                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.setServices(it)
                    }
                } else {
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }

    private fun fetchServices2() {
        lifecycleScope.launch {
            try {
                val service = CreateService.createService<DichVuService>()
                val response = withContext(Dispatchers.IO) { service.getListDichVu() }

                if (response.isSuccessful) {
                    response.body()?.let {
                        adapterServices.setServices(it)
                    }
                } else {
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }

    private fun handleError(code: Int, message: String) {
        Toast.makeText(this, "Lỗi [$code]: $message", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
