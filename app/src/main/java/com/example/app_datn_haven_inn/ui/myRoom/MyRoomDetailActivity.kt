package com.example.app_datn_haven_inn.ui.myRoom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.database.repository.TienNghiPhongRepository
import com.example.app_datn_haven_inn.database.service.TienNghiPhongService
import com.example.app_datn_haven_inn.databinding.ActivityMyRoomDetailBinding
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiPhongAdapter
import com.example.app_datn_haven_inn.utils.Constans
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyRoomDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyRoomDetailBinding
    private lateinit var tienNghiPhongAdapter: TienNghiPhongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ intent
        val roomData: LoaiPhongModel? = intent.getParcelableExtra("room_data")

        // Hiển thị dữ liệu phòng
        roomData?.let { room ->
            binding.txtTenPhongMr.text = room.tenLoaiPhong
            binding.tvDienTichMr.text = "${room.dienTich} m²"
            binding.txtNumberGuestMr.text = "${room.soLuongKhach} khách"
            binding.tvSLGiuongMr.text = room.giuong
            binding.viewpagerMr.adapter = RoomImageAdapter(room.hinhAnh)

            // Thiết lập RecyclerView
            setupRecyclerView()

            // Gọi API để lấy danh sách tiện nghi
            fetchTienNghiPhong(room.id)
        }

        // Nút quay lại
        binding.icBackMr.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        tienNghiPhongAdapter = TienNghiPhongAdapter(emptyList())
        binding.rvTiennghiphongMr.apply {
            layoutManager = LinearLayoutManager(this@MyRoomDetailActivity)
            adapter = tienNghiPhongAdapter
        }
    }

    private fun fetchTienNghiPhong(idLoaiPhong: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constans.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(TienNghiPhongService::class.java)
        val repository = TienNghiPhongRepository(service)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listTienNghi = repository.getListTienNghiByIdLoaiPhong(idLoaiPhong)
                withContext(Dispatchers.Main) {
                    if (!listTienNghi.isNullOrEmpty()) {
                        tienNghiPhongAdapter.items = listTienNghi
                        tienNghiPhongAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("MyRoomDetailActivity", "Không có tiện nghi nào cho phòng này.")
                    }
                }
            } catch (e: Exception) {
                Log.e("MyRoomDetailActivity", "Lỗi khi lấy danh sách tiện nghi: ${e.message}", e)
            }
        }
    }
}