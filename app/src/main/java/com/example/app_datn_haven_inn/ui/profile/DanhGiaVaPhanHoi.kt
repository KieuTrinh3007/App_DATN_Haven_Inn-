package com.example.app_datn_haven_inn.ui.profile

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.ui.review.adapter.ReviewAdapter
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DanhGiaVaPhanHoi : AppCompatActivity() {

    private val danhGiaService: DanhGiaService by lazy {
        CreateService.createService()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var danhGiaAdapter: ReviewAdapter
    private lateinit var img_back_DanhGia: ImageView
    var idNguoiDung: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danh_gia_va_phan_hoi)

        // Lấy idNguoiDung từ SharedPrefs
        idNguoiDung = SharedPrefsHelper.getIdNguoiDung(this)

        recyclerView = findViewById(R.id.recyclerView_DGvaPH)
        img_back_DanhGia = findViewById(R.id.img_back_DanhGia)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Kiểm tra xem idNguoiDung có tồn tại không
        if (idNguoiDung.isNullOrEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show()
            return
        }

        // Gọi API lấy danh sách đánh giá theo idNguoiDung
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Gọi API lấy danh sách đánh giá có idNguoiDung bằng idNguoiDung
                val response = danhGiaService.getListDanhGiaByIdUser(idNguoiDung!!)
                if (response.isSuccessful) {
                    val danhGiaList = response.body() ?: emptyList()

                    // Chuyển sang thread chính để cập nhật UI
                    withContext(Dispatchers.Main) {
                        if (danhGiaList.isNotEmpty()) {
                            // Cập nhật adapter với danh sách đánh giá
                            danhGiaAdapter = ReviewAdapter(danhGiaList)
                            recyclerView.adapter = danhGiaAdapter
                        } else {
                            Toast.makeText(this@DanhGiaVaPhanHoi, "Không có đánh giá nào", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Xử lý trường hợp API trả về lỗi
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DanhGiaVaPhanHoi, "Lỗi khi tải danh sách đánh giá", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ
                Log.e("DanhGiaVaPhanHoi", "Lỗi khi gọi API: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DanhGiaVaPhanHoi, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                }
            }
        }

        img_back_DanhGia.setOnClickListener{
            finish()
        }
    }
}
