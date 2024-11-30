package com.example.app_datn_haven_inn.ui.room

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.databinding.ActivityRoomDetailBinding
import com.example.app_datn_haven_inn.ui.review.adapter.ReviewAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.PhotoAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiPhongAdapter
import com.example.app_datn_haven_inn.viewModel.DanhGiaViewModel
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.TienNghiPhongViewModel
import java.util.Timer

class RoomDetailActivity : BaseActivity<ActivityRoomDetailBinding, BaseViewModel>() {
    private lateinit var photoAdapter: PhotoAdapter
    private var timer: Timer? = null
    private var mListphoto: List<String> = listOf()
    private var adapter: TienNghiPhongAdapter? = null
    private var adapterReview: ReviewAdapter? = null
    private var tienNghiViewModel: TienNghiPhongViewModel? = null
    private var loaiPhongViewModel: LoaiPhongViewModel? = null
    private var danhGiaViewModel: DanhGiaViewModel? = null
    private var isFavorite = false
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
        danhGiaViewModel = ViewModelProvider(this)[DanhGiaViewModel::class.java]
        adapter = TienNghiPhongAdapter(listOf())
        binding.rvTiennghiphong.adapter = adapter


        adapterReview = ReviewAdapter(listOf())
        binding.rvReview.adapter = adapterReview

        val tvTenPhong = intent.getStringExtra("tenLoaiPhong").toString()
        val tvSLGiuong = intent.getStringExtra("giuong").toString()
        val tvSLKhach = intent.getStringExtra("soLuongKhach").toString() + " Khách"
        val tvDienTich = intent.getStringExtra("dienTich").toString() + " mét vuông"
        val hinhAnh = intent.getStringArrayExtra("hinhAnh")
        val moTa = intent.getStringExtra("moTa")

        binding.txtTenPhong.text = tvTenPhong
        binding.tvSLGiuong.text = tvSLGiuong
        binding.txtNumberGuest.text = tvSLKhach
        binding.tvDienTich.text = tvDienTich
        binding.tvMoTa.text = moTa


        Log.d("hinhAnh", hinhAnh.toString())
        if (hinhAnh != null) {
            mListphoto = hinhAnh.toList()
            photoAdapter = PhotoAdapter(this, hinhAnh.toList())
            binding.viewpager.adapter = photoAdapter
            binding.circleIndicator.setViewPager(binding.viewpager)
            photoAdapter.registerDataSetObserver(binding.circleIndicator.dataSetObserver)
        }


        tienNghiViewModel?.getListTienNghiPhongByIdLoaiPhong(idLoaiPhong.toString())
        tienNghiViewModel?.tienNghiPhongListByIdLoaiPhong?.observe(this) { list ->
            Log.d("tienNghiPhongViewModel", "List size: ${list?.size}")
            if (list != null) {

                adapter?.let {
                    it.items = list
                    it.notifyDataSetChanged()
                }

            }
        }


        danhGiaViewModel?.getListdanhGiaByIdLoaiPhong(idLoaiPhong.toString())
        danhGiaViewModel?.danhGiaListByIdLoaiPhong?.observe(this) { review ->
            if (review != null) {

                val soDanhGia = review.size
                val firstTwoReviews = review.take(2)

                binding.txtNumberReview1.text = "Có $soDanhGia nhận xét"
                binding.txtNumberReview.text = "$soDanhGia nhận xét"
                if (soDanhGia > 2) {

                    binding.txtSeeAllReviews.visibility = View.VISIBLE
                } else {
                    binding.txtSeeAllReviews.visibility = View.GONE
                }

                val totalPoints = review.sumOf { it.soDiem }
                val averageRating = if (review.isNotEmpty()) totalPoints.toFloat() / review.size else 0f

                // Cập nhật điểm trung bình vào TextView txtRating
                binding.txtRating.text = String.format("%.1f", averageRating)
                binding.txtRating1.text = String.format("%.1f", averageRating)

                adapterReview?.let {
                    it.listReview = firstTwoReviews
                    it.notifyDataSetChanged()
                }

                binding.txtSeeAllReviews.setOnClickListener {
                    // Khi bấm vào "See All", hiển thị tất cả nhận xét
                    adapterReview?.let {
                        it.listReview = review // Gán toàn bộ danh sách nhận xét
                        it.notifyDataSetChanged()
                    }

                    // Ẩn nút "Xem tất cả nhận xét" sau khi nhấn
                    binding.txtSeeAllReviews.visibility = View.GONE
                }
            }
        }


        binding.icBack.setOnClickListener {
            finish()
        }

        binding.btnBack.setOnClickListener {
            moveToPreviousSlide()
        }

        binding.btnNext.setOnClickListener {
            moveToNextSlide()
        }


        binding.btnTuyChinh.setOnClickListener {
            startActivity(Intent(this, TuyChinhDatPhongActivity::class.java))
        }



    }


    private fun moveToNextSlide() {
        if (mListphoto.isEmpty()) return
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        binding.viewpager.currentItem = if (currentItem < totalItem) {
            currentItem + 1
        } else {
            0
        }
    }

    private fun moveToPreviousSlide() {
        if (mListphoto.isEmpty()) return
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        binding.viewpager.currentItem = if (currentItem > 0) {
            currentItem - 1
        } else {
            totalItem
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }


}