package com.example.app_datn_haven_inn.ui.favorite

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import com.example.app_datn_haven_inn.databinding.FragmentFavoriteBinding
import com.example.app_datn_haven_inn.ui.room.adapter.PhongNghiAdapter
import com.example.app_datn_haven_inn.viewModel.DanhGiaViewModel
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: YeuThichViewModel
    private var adapter: PhongNghiAdapter? = null
    private lateinit var danhGiaViewModel: DanhGiaViewModel
    private var listLoaiPhongModel: MutableList<LoaiPhongModel> = mutableListOf()

    override fun inflateViewBinding(): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()
        val danhGiaMap = mutableMapOf<String, Pair<Double, Int>>()
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", "")
        Log.d("FavoriteFragment", "idNguoiDung: $idUser")
        danhGiaViewModel = ViewModelProvider(this)[DanhGiaViewModel::class.java]

        viewModel = ViewModelProvider(this)[YeuThichViewModel::class.java]
        viewModel.getFavoritesByUserId(idUser.toString())
        adapter = PhongNghiAdapter(mutableListOf(), requireContext(), danhGiaMap)

        viewModel.yeuThichList1.observe(viewLifecycleOwner) { yeuThichList ->
            if (yeuThichList.isNullOrEmpty()) {
                viewBinding.rvFavorite.visibility = View.GONE
                viewBinding.ivNoData.visibility = View.VISIBLE

            } else {
                viewBinding.rvFavorite.visibility = View.VISIBLE
                viewBinding.ivNoData.visibility = View.GONE
                Log.d("FavoriteFragment", "yeuThichList: $yeuThichList")
                if (yeuThichList != null) {
                    listLoaiPhongModel.clear()  // Đảm bảo làm sạch trước khi thêm mới
                    listLoaiPhongModel.addAll(yeuThichList)

                    // Lặp qua tất cả các phòng và lấy đánh giá
                    val remainingRooms = listLoaiPhongModel.size
                    var processedRooms = 0
                    Log.d("FavoriteFragment", "idLoaiPhong: ${listLoaiPhongModel.size}")
                    listLoaiPhongModel.forEach { phong ->
                        danhGiaViewModel.getListdanhGiaByIdLoaiPhong(phong.id)
                        Log.d("FavoriteFragment", "idLoaiPhong: ${phong.id}")
                    }

                    // Quan sát danh sách đánh giá từ ViewModel
                    danhGiaViewModel.danhGiaListByIdLoaiPhong.observe(viewLifecycleOwner) { danhGiaList ->

                        danhGiaList?.forEach { danhGia ->
                            val phongId = danhGia.id_LoaiPhong
                            val soDiem = danhGiaList.sumOf { it.soDiem }
                            val soLuongDanhGia = danhGiaList.size
                            val diemTrungBinh =
                                if (soLuongDanhGia > 0) soDiem / soLuongDanhGia.toDouble() else 0.0

                            danhGiaMap[phongId] = Pair(diemTrungBinh, soLuongDanhGia)
                        }

                        processedRooms++

                        adapter?.updateList(yeuThichList.toMutableList())
                        viewBinding.rvFavorite.adapter = adapter

                    }
                }

            }
        }

        adapter?.setonFavotiteSelected { loaiPhong ->
            // Gọi hàm xóa yêu thích
            viewModel.deleteyeuThich(loaiPhong.id, idUser.toString())

            // Quan sát danh sách yêu thích và cập nhật giao diện
            viewModel.yeuThichList1.observe(viewLifecycleOwner) { updatedList ->
                if (updatedList.isNullOrEmpty()) {
                    viewBinding.rvFavorite.visibility = View.GONE
                    viewBinding.ivNoData.visibility = View.VISIBLE
                } else {
                    adapter?.updateList(updatedList.toMutableList())
                    viewBinding.rvFavorite.visibility = View.VISIBLE
                    viewBinding.ivNoData.visibility = View.GONE
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", "")
        viewModel.getFavoritesByUserId(idUser.toString())
        viewModel.yeuThichList1.observe(viewLifecycleOwner) { yeuThichList ->
            if (yeuThichList.isNullOrEmpty()) {
                viewBinding.rvFavorite.visibility = View.GONE
                viewBinding.ivNoData.visibility = View.VISIBLE
            } else {
                viewBinding.rvFavorite.visibility = View.VISIBLE
                viewBinding.ivNoData.visibility = View.GONE
                adapter?.updateList(yeuThichList.toMutableList())
                yeuThichList.forEach { phong ->
                    adapter?.updateFavoriteState(itemId = phong.id, phong.isFavorite)
//
                }

                viewBinding.rvFavorite.adapter = adapter
            }
        }

    }


}