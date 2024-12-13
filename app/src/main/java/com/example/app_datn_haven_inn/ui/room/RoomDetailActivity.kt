package com.example.app_datn_haven_inn.ui.room

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.database.model.FavoriteRequest
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.ActivityRoomDetailBinding
import com.example.app_datn_haven_inn.dialog.DialogSignIn
import com.example.app_datn_haven_inn.ui.auth.SignIn
import com.example.app_datn_haven_inn.ui.review.adapter.ReviewAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.PhotoAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiPhongAdapter
import com.example.app_datn_haven_inn.utils.SharePrefUtils
import com.example.app_datn_haven_inn.utils.SharePrefUtils.saveFavoriteState
import com.example.app_datn_haven_inn.viewModel.DanhGiaViewModel
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.TienNghiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel
import java.util.Locale
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
    private lateinit var yeuThichViewModel: YeuThichViewModel
    private var isFavorite = false
    private var averageRating: Double = 0.0
    private var soDanhGia: Int = 0
    override fun createBinding(): ActivityRoomDetailBinding {
        return ActivityRoomDetailBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        yeuThichViewModel = ViewModelProvider(this)[YeuThichViewModel::class.java]
        val idLoaiPhong = intent.getStringExtra("id_LoaiPhong")
        loaiPhongViewModel = ViewModelProvider(this)[LoaiPhongViewModel::class.java]
        tienNghiViewModel = ViewModelProvider(this)[TienNghiPhongViewModel::class.java]
        danhGiaViewModel = ViewModelProvider(this)[DanhGiaViewModel::class.java]

        // list tien nghi phong
        adapter = TienNghiPhongAdapter(listOf())
        binding.rvTiennghiphong.adapter = adapter

        // list binh luan
        adapterReview = ReviewAdapter(listOf())
        binding.rvReview.adapter = adapterReview

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", null)

        val tvTenPhong = intent.getStringExtra("tenLoaiPhong").toString()
        val tvSLGiuong = intent.getStringExtra("giuong").toString()
        val tvSLKhach = intent.getStringExtra("soLuongKhach").toString() + " Khách"
        val tvDienTich = intent.getStringExtra("dienTich").toString() + " mét vuông"
        val hinhAnh = intent.getStringArrayExtra("hinhAnh")
        val tvGiaTien = intent.getIntExtra("giaTien", 0)
        val moTa = intent.getStringExtra("moTa")
        val danhGiaMap = mutableMapOf<String, Pair<Double, Int>>()
        isFavorite = intent.getBooleanExtra("isFavorite", false)

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

                soDanhGia = review.size
                val firstTwoReviews = review.take(2)

                binding.txtNumberReview1.text = "$soDanhGia nhận xét"
                binding.txtNumberReview.text = "$soDanhGia nhận xét"
                if (soDanhGia > 2) {

                    binding.txtSeeAllReviews.visibility = View.VISIBLE
                } else {
                    binding.txtSeeAllReviews.visibility = View.GONE
                }

                val totalPoints = review.sumOf { it.soDiem }
                averageRating =
                    if (review.isNotEmpty()) totalPoints.toDouble() / review.size else 0.0

                // Cập nhật điểm trung bình vào TextView txtRating
                binding.txtRating.text = String.format(Locale.US, "%.1f", averageRating)
                binding.txtRating1.text = String.format(Locale.US, "%.1f", averageRating)

                val emotion = when {
                    averageRating >= 9.0 -> "Tuyệt vời"
                    averageRating >= 7.0 -> "Tốt"
                    averageRating >= 5.0 -> "Bình thường"
                    averageRating >= 3.0 -> "Tệ"
                    else -> "Rất tệ"
                }
                binding.txtCamxuc.text = emotion
                danhGiaMap[idLoaiPhong.toString()] = Pair(averageRating, soDanhGia)

                adapterReview?.let {
                    it.listReview = firstTwoReviews
                    it.notifyDataSetChanged()
                }

                binding.txtSeeAllReviews.setOnClickListener {
                    adapterReview?.let {
                        it.listReview = review
                        it.notifyDataSetChanged()
                    }
                    binding.txtSeeAllReviews.visibility = View.GONE
                }
            }
        }


        binding.icBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("itemId", intent.getStringExtra("id_LoaiPhong"))
            resultIntent.putExtra("isFavorite", isFavorite)
            resultIntent.putExtra("diem", averageRating)
            resultIntent.putExtra("soDanhGia", soDanhGia)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }

        binding.btnBack.setOnClickListener {
            moveToPreviousSlide()
        }

        binding.btnNext.setOnClickListener {
            moveToNextSlide()
        }


        checkIfFavorite(idLoaiPhong)

        binding.btnAddFavorite.setOnClickListener {
            if (idUser.isNullOrEmpty()) {
                val dialog = DialogSignIn(this)
                dialog.show()
                return@setOnClickListener
            }

            val params = binding.btnTuyChinh.layoutParams as LinearLayout.LayoutParams
            params.weight = 2f
            params.marginStart = 0
            binding.btnTuyChinh.layoutParams = params

//            if (idUser.isNullOrEmpty()) {
//                Toast.makeText(this, "Vui lòng đăng nhập để thêm yêu thích", Toast.LENGTH_SHORT)
//                    .show()
//                return@setOnClickListener
//            }

            val yeuThich = FavoriteRequest(idUser, idLoaiPhong.toString())
            yeuThichViewModel.addyeuThich(yeuThich)
            yeuThichViewModel.isyeuThichAdded.observe(this) { success ->
                if (success) {
                    // Cập nhật trạng thái isFavorite
                    isFavorite = true
                    val currentRoom = LoaiPhongModel(
                        id = idLoaiPhong ?: "",
                        tenLoaiPhong = tvTenPhong,
                        giuong = tvSLGiuong,
                        soLuongKhach = tvSLKhach.toIntOrNull() ?: 0,
                        dienTich = tvDienTich.replace(" mét vuông", "").toDoubleOrNull() ?: 0.0,
                        hinhAnh = ArrayList(mListphoto),
                        hinhAnhIDs = arrayListOf(),
                        giaTien = 0.0,
                        moTa = moTa ?: "",
                        trangThai = true,
                        isFavorite = true
                    )
                    saveFavoriteState(this, currentRoom)
                    // Cập nhật lại giao diện
                    binding.btnAddFavorite.visibility = View.GONE
                    Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        binding.btnTuyChinh.setOnClickListener {
            if (idUser.isNullOrEmpty()) {
                val dialog = DialogSignIn(this)
                dialog.show()

                return@setOnClickListener
            }
            val intent = Intent(this, TuyChinhDatPhongActivity::class.java)
            intent.putExtra("id_LoaiPhong", idLoaiPhong)
            intent.putExtra("giaTien", tvGiaTien)
            intent.putExtra("soLuongKhach", tvSLKhach)
            startActivity(intent)
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

    private fun checkIfFavorite(idLoaiPhong: String?) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", "")

        yeuThichViewModel.getFavoritesByUserId(idUser.toString())
        yeuThichViewModel.yeuThichList1.observe(this) { yeuThichList ->
            if (yeuThichList != null) {

                isFavorite = yeuThichList.any { it.id == idLoaiPhong }

                // Cập nhật trạng thái cho nút thêm yêu thích
                if (isFavorite) {
                    binding.btnAddFavorite.visibility = View.GONE
                    val params = binding.btnTuyChinh.layoutParams as LinearLayout.LayoutParams
                    params.weight = 2f
                    params.marginStart = 0
                    binding.btnTuyChinh.layoutParams = params
                } else {
                    binding.btnAddFavorite.visibility = View.VISIBLE
                    val params = binding.btnTuyChinh.layoutParams as LinearLayout.LayoutParams
                    params.weight = 1f
                    binding.btnTuyChinh.layoutParams = params
                }
            }
        }
    }
}