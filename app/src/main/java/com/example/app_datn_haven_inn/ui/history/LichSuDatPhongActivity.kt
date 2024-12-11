package com.example.app_datn_haven_inn.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityLichSuDatPhongBinding
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.service.HoaDonService
import com.example.app_datn_haven_inn.utils.Constans
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LichSuDatPhongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLichSuDatPhongBinding
    private lateinit var adapter: LichSuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityLichSuDatPhongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerViewLs.layoutManager = LinearLayoutManager(this)

        // Set up Back Button
//        binding.icBackLichSu.setOnClickListener {
//            onBackPressed() // Handles the back navigation
//        }

        // Fetch booking history data
        fetchHistory()
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

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Gọi API
                val response = service.getHistory(userId)

                // Chuyển về luồng chính để xử lý giao diện
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Lấy dữ liệu từ API
                        val historyList = response.body()

                        if (historyList.isNullOrEmpty()) {
                            // Không có dữ liệu, hiển thị TextView thông báo
                            binding.recyclerViewLs.visibility = View.GONE
                            binding.tvEmptyHistory.visibility = View.VISIBLE
                            showMessage("Không có lịch sử đặt phòng")
                        } else {
                            // Có dữ liệu, hiển thị danh sách
                            adapter = LichSuAdapter(historyList)
                            binding.recyclerViewLs.adapter = adapter
                            binding.recyclerViewLs.visibility = View.VISIBLE
                            binding.tvEmptyHistory.visibility = View.GONE
                        }
                    } else {
                        // Lỗi từ API (response.isSuccessful == false)
                        Log.e("HistoryActivity", "API Error: ${response.errorBody()?.string()}")
                        showMessage("Lỗi khi tải lịch sử")
                    }
                }
            } catch (e: Exception) {
                // Lỗi khi gọi API hoặc ngoại lệ
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
