package com.example.app_datn_haven_inn.ui.home.Faragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.TienNghiModel
import com.example.app_datn_haven_inn.database.service.DichVuService
import com.example.app_datn_haven_inn.database.service.TienNghiService
import com.example.app_datn_haven_inn.databinding.FragmentServiceBinding
import com.example.app_datn_haven_inn.ui.home.adapter.ServicesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ServiceAdapter
    private lateinit var adapterServices: ServicesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter cho RecyclerView đầu tiên
        adapter = ServiceAdapter()
        binding.recyclerViewService.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewService.adapter = adapter

        // Adapter cho RecyclerView thứ hai
        adapterServices = ServicesAdapter()
        binding.recyclerViewServices.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewServices.adapter = adapterServices

        // Gọi API cho cả hai RecyclerView
        fetchServices()
        fetchServices2()
    }

    private fun fetchServices() {
        lifecycleScope.launch {
            try {
                // Sử dụng CreateService để tạo service
                val service = CreateService.createService<TienNghiService>()
                val response = withContext(Dispatchers.IO) { service.getListTienNghi() }

                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.setServices(it) // Cập nhật dữ liệu lên Adapter
                    }
                } else {
                    // Xử lý lỗi nếu response không thành công
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Xử lý ngoại lệ trong quá trình gọi API
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }

    private fun fetchServices2() {
        lifecycleScope.launch {
            try {
                val service = CreateService.createService<DichVuService>()

                // Gọi API
                val response = withContext(Dispatchers.IO) { service.getListDichVu() }

                if (response.isSuccessful) {
                    response.body()?.let { list ->
                        adapterServices.setServices(list) // Hiển thị toàn bộ danh sách
                    }
                } else {
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }


    private fun handleError(code: Int, message: String) {
        // Hiển thị thông báo lỗi hoặc log lỗi
        println("Error [$code]: $message")
    }

    private fun showError(message: String) {
        // Ví dụ: Hiển thị thông báo qua Toast
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
