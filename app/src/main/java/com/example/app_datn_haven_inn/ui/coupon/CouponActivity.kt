package com.example.app_datn_haven_inn.ui.coupon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.databinding.ActivityCouponBinding
import com.example.app_datn_haven_inn.viewModel.CouponViewModel
import com.example.app_datn_haven_inn.database.model.CouponModel

class CouponActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCouponBinding
    private lateinit var couponAdapter: CouponAdapter
    private val couponViewModel: CouponViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng View Binding để thiết lập giao diện
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập RecyclerView
        couponAdapter = CouponAdapter(emptyList()) { coupon ->
            // Thao tác khi người dùng nhấn nút "Sử dụng ngay"
            Toast.makeText(this, "Đã chọn mã: ${coupon.maGiamGia}", Toast.LENGTH_SHORT).show()

            // Tạo Intent để trả lại mã giảm giá cho màn hình trước
            val resultIntent = Intent()
            resultIntent.putExtra("couponCode", coupon.maGiamGia) // Truyền mã giảm giá
            resultIntent.putExtra("idCoupon", coupon.id)
            setResult(Activity.RESULT_OK, resultIntent) // Đặt kết quả và trả lại
            finish() // Đóng màn hình CouponActivity
        }

        binding.recyclerView.apply {
            adapter = couponAdapter
            layoutManager = LinearLayoutManager(this@CouponActivity)
        }

        // Quan sát danh sách mã giảm giá từ ViewModel
        couponViewModel.couponList.observe(this) { couponList ->
            couponList?.let {
                couponAdapter.updateCoupons(it)
                binding.progressBar.visibility = View.GONE // Ẩn ProgressBar khi có dữ liệu
            }
        }

        // Quan sát thông báo lỗi từ ViewModel
        couponViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE // Ẩn ProgressBar khi gặp lỗi
            }
        }

        // Hiển thị ProgressBar khi bắt đầu tải dữ liệu
        binding.progressBar.visibility = View.VISIBLE

        // Gọi ViewModel để lấy danh sách mã giảm giá
        couponViewModel.getCouponListByUser(this)

        // Xử lý sự kiện quay lại khi nhấn nút back
        binding.icBackCp.setOnClickListener {
            onBackPressed() // Xử lý quay lại màn hình trước
        }
    }
}
