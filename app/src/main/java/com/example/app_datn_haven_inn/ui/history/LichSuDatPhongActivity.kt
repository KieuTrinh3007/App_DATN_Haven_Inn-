package com.example.app_datn_haven_inn.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.HoaDonModel1
import com.example.app_datn_haven_inn.database.service.HoaDonService
import com.example.app_datn_haven_inn.databinding.ActivityLichSuDatPhongBinding
import com.example.app_datn_haven_inn.utils.Constans
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LichSuDatPhongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLichSuDatPhongBinding
    private lateinit var adapter: LichSuAdapter
    private var selectedStatus: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLichSuDatPhongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedStatus = 1
        // Khởi tạo RecyclerView
        binding.recyclerViewLs.layoutManager = LinearLayoutManager(this)
        adapter = LichSuAdapter(mutableListOf()) { hoaDon ->
            // Xử lý khi nhấn vào lịch sử, nếu cần
        }
        binding.recyclerViewLs.adapter = adapter

        setupTabLayout()
        fetchHistory()
    }

    private fun setupTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã thanh toán"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã nhận phòng"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã hủy"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedStatus = when (tab.position) {
                    0 -> 1 // Đã thanh toán
                    1 -> 0 // Đã nhận phòng
                    2 -> 2 // Đã hủy
                    else -> null
                }
                fetchHistory() // Fetch lại dữ liệu sau khi chọn tab
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun fetchHistory() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constans.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(HoaDonService::class.java)
        val userId = SharedPrefsHelper.getIdNguoiDung(this)

        if (userId.isNullOrEmpty()) {
            showMessage("Không tìm thấy ID người dùng!")
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewLs.visibility = View.GONE
        binding.tvEmptyHistory.visibility = View.GONE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Gọi API để lấy lịch sử đặt phòng
                val response = service.getHistory(userId, null) // Lấy tất cả lịch sử
                withContext(Dispatchers.Main) {

                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val historyList = response.body() ?: emptyList()
                        Log.d("HistoryResponse", "Data received: $historyList") // Ghi log dữ liệu

                        // Lọc danh sách theo trạng thái đã chọn
                        val filteredList = historyList.filter { it.trangThai == selectedStatus }

                        adapter.updateData(filteredList) // Cập nhật danh sách đã lọc

                        // Kiểm tra xem danh sách có rỗng hay không
                        if (filteredList.isEmpty()) {
                            binding.recyclerViewLs.visibility = View.GONE
                            binding.tvEmptyHistory.visibility = View.VISIBLE
                            showMessage("Không có dữ liệu.")
                        } else {
                            binding.recyclerViewLs.visibility = View.VISIBLE
                            binding.tvEmptyHistory.visibility = View.GONE
                        }
                    } else {
                        Log.e("HistoryActivity", "API Error: ${response.errorBody()?.string()}")
                        showMessage("Lỗi khi tải lịch sử")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HistoryActivity", "Exception: ${e.message}", e)
                    showMessage("Có lỗi xảy ra khi tải lịch sử. Vui lòng thử lại!")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}