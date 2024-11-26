package com.example.app_datn_haven_inn.ui.room

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityRoomDetailBinding
import com.example.app_datn_haven_inn.ui.review.adapter.ReviewAdapter
import com.example.app_datn_haven_inn.ui.review.model.Review
import com.example.app_datn_haven_inn.ui.room.adapter.PhotoAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiPhongAdapter
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.TienNghiPhongViewModel
import java.util.Timer

class RoomDetailActivity : BaseActivity<ActivityRoomDetailBinding, BaseViewModel>() {
    private lateinit var photoAdapter: PhotoAdapter
    private var timer: Timer? = null
    private var mListphoto: List<String> = listOf()
    private var adapter: TienNghiPhongAdapter? = null
    private var tienNghiViewModel: TienNghiPhongViewModel? = null
    private var loaiPhongViewModel: LoaiPhongViewModel? = null
    override fun createBinding(): ActivityRoomDetailBinding {
        return ActivityRoomDetailBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        val idLoaiPhong = intent.getStringExtra("id_LoaiPhong")
        loaiPhongViewModel = ViewModelProvider(this)[LoaiPhongViewModel::class.java]
        tienNghiViewModel = ViewModelProvider(this)[TienNghiPhongViewModel::class.java]
        adapter = TienNghiPhongAdapter(listOf())
        binding.rvTiennghiphong.adapter = adapter

        val tvTenPhong = intent.getStringExtra("tenLoaiPhong").toString()
        val tvSLGiuong = intent.getStringExtra("giuong").toString()
        val tvSLKhach = intent.getStringExtra("soLuongKhach").toString() + " Khách"
        val tvDienTich = intent.getStringExtra("dienTich").toString() + " mét vuông"
        val hinhAnh = intent.getStringArrayExtra("hinhAnh")

        binding.txtTenPhong.text = tvTenPhong
        binding.tvSLGiuong.text = tvSLGiuong
        binding.txtNumberGuest.text = tvSLKhach
        binding.tvDienTich.text = tvDienTich


        Log.d("hinhAnh", hinhAnh.toString())
        if (hinhAnh != null) {
            photoAdapter = PhotoAdapter(this, hinhAnh.toList())
            binding.viewpager.adapter = photoAdapter
            binding.circleIndicator.setViewPager(binding.viewpager)
            photoAdapter.registerDataSetObserver(binding.circleIndicator.dataSetObserver)
        }


        tienNghiViewModel?.getListTienNghiPhongByIdLoaiPhong(idLoaiPhong.toString())
        tienNghiViewModel?.tienNghiPhongListByIdLoaiPhong?.observe(this) { list ->
            if (list != null) {

                adapter?.let {
                    it.items = list
                    it.notifyDataSetChanged()
                }
            }
        }

        tienNghiViewModel?.getListtienNghiPhong()
        tienNghiViewModel?.tienNghiPhongList?.observe(this) { tienNghiList ->
            if (tienNghiList != null) {
                adapter?.let {
                    it.items = tienNghiList
                    it.notifyDataSetChanged()
                }

            }
        }


        binding.icBack.setOnClickListener {
            finish()
        }

        // Set up button click events
        binding.btnBack.setOnClickListener {
            moveToPreviousSlide()
        }

        binding.btnNext.setOnClickListener {
            moveToNextSlide()
        }

        loadReview()



        binding.btnTuyChinh.setOnClickListener {
            startActivity(Intent(this, TuyChinhDatPhongActivity::class.java))
        }

        // Gạch ngang cho TextView
        binding.txtGiaCu.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

    }


    private fun loadReview() {
        val allReviews = listOf(
            Review(
                R.drawable.avat2,
                "Lê Đăng Sang",
                9,
                "Khách sạn có dịch vụ rất tốt, nhân viên nhiệt tình lịch sự, phòng ốc trang nhã sạch sẽ",
                "12/10/2024"
            ),
            Review(R.drawable.avatar1, "Vũ Thị Vân Anh", 10, "Good place to stay", "1/10/2024"),
            Review(R.drawable.ic_person, "Nguyễn Văn A", 8, "Dịch vụ tốt!", "01/11/2023"),
            Review(R.drawable.person13x13, "Trần Thị B", 9, "Rất hài lòng.", "15/11/2023")
        )

        // Lấy 2 bình luận đầu tiên để hiển thị ban đầu
        val initialReviews = allReviews.take(2)

        val adapter = ReviewAdapter(initialReviews.toMutableList())
        binding.rvReview.layoutManager = LinearLayoutManager(this)
        binding.rvReview.adapter = adapter

        // Kiểm tra nếu có hơn 2 bình luận để hiển thị TextView "Xem tất cả bình luận"
        if (allReviews.size > 2) {
            binding.txtSeeAllReviews.visibility = View.VISIBLE

            // Xử lý sự kiện bấm vào "Xem tất cả bình luận"
            binding.txtSeeAllReviews.setOnClickListener {
                adapter.updateReviews(allReviews)  // Cập nhật RecyclerView với tất cả bình luận
                binding.txtSeeAllReviews.visibility = View.GONE  // Ẩn TextView sau khi bấm
            }
        } else {
            binding.txtSeeAllReviews.visibility = View.GONE
        }
    }


    private fun moveToNextSlide() {
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        if (currentItem < totalItem) {
            binding.viewpager.currentItem = currentItem + 1
        } else {
            binding.viewpager.currentItem = 0
        }
    }

    private fun moveToPreviousSlide() {
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        if (currentItem > 0) {
            binding.viewpager.currentItem = currentItem - 1
        } else {
            binding.viewpager.currentItem = totalItem
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }


}