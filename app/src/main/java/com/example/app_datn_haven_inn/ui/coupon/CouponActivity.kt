package com.example.app_datn_haven_inn.ui.coupon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        var bookingPrice = intent.getDoubleExtra("giaGoc", 0.0)
        val trangthai = intent.getIntExtra("trangthai", 0)

        // Thiết lập RecyclerView
        couponAdapter = CouponAdapter(emptyList(), { coupon ->
            // Tạo Intent để trả lại mã giảm giá cho màn hình trước
            if (bookingPrice >= coupon.dieuKienToiThieu) {
                // Nếu giá tiền thỏa mãn, thực hiện áp dụng mã giảm giá
                Toast.makeText(this, "Đã chọn mã: ${coupon.maGiamGia}", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("couponCode", coupon.maGiamGia)
                resultIntent.putExtra("couponId", coupon.id_cp)
                resultIntent.putExtra("giamGia", coupon.giamGia.toString())
                resultIntent.putExtra("giamGiaToiDa", coupon.giamGiaToiDa.toString())
                setResult(Activity.RESULT_OK, resultIntent)

                finish() // Đóng màn hình CouponActivity
            } else {
                // Nếu không thỏa mãn, hiển thị thông báo
                Toast.makeText(this, "Coupon không áp dụng cho hóa đơn này", Toast.LENGTH_SHORT).show()
            }
        }, trangthai) // Truyền trạng thái vào adapter


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
