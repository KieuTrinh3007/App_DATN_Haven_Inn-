package com.example.app_datn_haven_inn.ui.history

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.service.DanhGiaService
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
import java.util.UUID

class LichSuDatPhongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLichSuDatPhongBinding
    private lateinit var adapter: LichSuAdapter
    private var selectedStatus: Int? = null

    private val danhGiaService = CreateService.createService<DanhGiaService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLichSuDatPhongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedStatus = 1
        // Khởi tạo RecyclerView
        binding.recyclerViewLs.layoutManager = LinearLayoutManager(this)
        adapter = LichSuAdapter(mutableListOf(), { hoaDon ->
            // Xử lý khi nhấn vào lịch sử, nếu cần
        }, this, ::openDanhGiaDialog)
        binding.recyclerViewLs.adapter = adapter

        binding.btnBack.setOnClickListener{
            finish()
        }

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

    private fun openDanhGiaDialog(idNguoiDung: String, idLoaiPhong: String, ngayDanhGia: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_danh_gia)
        dialog.setCancelable(true)

        // Áp dụng nền trong suốt cho dialog
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Thay đổi kích thước dialog (Chiều rộng và chiều cao)
        val params = dialog.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.9).toInt() // Chiều rộng dialog chiếm 90% màn hình
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT // Chiều cao tự động
        dialog.window?.attributes = params

        val ratingBar = dialog.findViewById<RatingBar>(R.id.rating_bar)
        val etComment = dialog.findViewById<EditText>(R.id.et_comment)
        val btnSubmit = dialog.findViewById<TextView>(R.id.btn_submit)
        val ivFeedbackIcon = dialog.findViewById<ImageView>(R.id.iv_feedback_icon)

        val feedbackAnimation = ObjectAnimator.ofFloat(ivFeedbackIcon, "scaleX", 0.5f, 1f).apply {
            duration = 300
        }
        val feedbackAnimationY = ObjectAnimator.ofFloat(ivFeedbackIcon, "scaleY", 0.5f, 1f).apply {
            duration = 300
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Cập nhật biểu tượng cảm xúc dựa trên số sao đã chọn
            val feedbackIcon = when {
                rating >= 4 -> R.drawable.happiness // 4 sao hoặc hơn -> Cảm xúc vui
                rating >= 2 -> R.drawable.face // Từ 2 đến 4 sao -> Cảm xúc trung bình
                else -> R.drawable.vanh1 // Dưới 2 sao -> Cảm xúc buồn
            }
            ivFeedbackIcon.setImageResource(feedbackIcon)

            // Áp dụng hiệu ứng chuyển động
            feedbackAnimation.start()
            feedbackAnimationY.start()
        }

        btnSubmit.setOnClickListener {
            val soDiem = ratingBar.rating * 2 // Quy đổi ra điểm (1 sao = 2 điểm)
            val binhLuan = etComment.text.toString()

            if (soDiem.toDouble() == 0.0) {
                return@setOnClickListener
            }

            val danhGia = DanhGiaModel(
                id = UUID.randomUUID().toString(),
                id_NguoiDung = idNguoiDung,
                id_LoaiPhong = idLoaiPhong,
                soDiem = soDiem.toDouble(),
                binhLuan = binhLuan,
                ngayDanhGia = ngayDanhGia
            )

            lifecycleScope.launch {
                try {
                    val response =
                        withContext(Dispatchers.IO) { danhGiaService.addDanhGia(danhGia) }
                    if (response.isSuccessful) {
                        Toast.makeText(this@LichSuDatPhongActivity, "Cảm ơn bạn đã đánh giá.", Toast.LENGTH_SHORT)
                            .show()
                        fetchHistory()
                        dialog.dismiss()
                    } else {
                        showError("Gửi đánh giá thất bại!")
                    }
                } catch (e: Exception) {
                    showError("Có lỗi xảy ra: ${e.message}")
                }
            }
        }

        // Tạo hiệu ứng mở dialog mượt mà
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
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
                            showMessage("không có lịch sử đặt phòng.")
                        } else {
                            binding.recyclerViewLs.visibility = View.VISIBLE
                            binding.tvEmptyHistory.visibility = View.GONE
                        }
                    } else {
                        Log.e("HistoryActivity", "API Error: ${response.errorBody()?.string()}")
                        showMessage("không có lịch sử đặt phòng.")
                        binding.recyclerViewLs.visibility = View.GONE
                        binding.tvEmptyHistory.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HistoryActivity", "Exception: ${e.message}", e)
                    showMessage("không có lịch sử đặt phòng!")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}