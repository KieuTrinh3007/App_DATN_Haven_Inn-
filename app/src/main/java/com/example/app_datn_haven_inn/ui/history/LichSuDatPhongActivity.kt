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
        fetchBookingHistory()
    }

    private fun fetchBookingHistory() {
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
                val response = service.getListHoaDon()
                if (response.isSuccessful) {
                    val hoaDonList = response.body()

                    if (hoaDonList.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            showMessage("Không có lịch sử đặt phòng nào")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            // Set the adapter with the booking history list
                            adapter = LichSuAdapter(hoaDonList)
                            binding.recyclerViewLs.adapter = adapter
                        }
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("LichSuDatPhongActivity", "API Error: $errorResponse")
                    withContext(Dispatchers.Main) {
                        showMessage("Lỗi khi tải danh sách lịch sử: $errorResponse")
                    }
                }
            } catch (e: Exception) {
                Log.e("LichSuDatPhongActivity", "Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    showMessage("Có lỗi xảy ra khi tải danh sách lịch sử. Vui lòng thử lại!")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
