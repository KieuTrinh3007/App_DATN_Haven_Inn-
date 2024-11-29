package com.example.app_datn_haven_inn.ui.favorite

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.FragmentFavoriteBinding
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: YeuThichViewModel
    private var adapter = FavoriteAdapter(
        mutableListOf(),
        onFavoriteClick = { loaiPhong, position ->
        }
    )


    override fun inflateViewBinding(): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", "")
        Log.d("PhongNghiFragment", "idNguoiDung: $idUser")

        viewModel = ViewModelProvider(this)[YeuThichViewModel::class.java]
        viewModel.getFavoritesByUserId(idUser.toString())




        viewModel.yeuThichList1.observe(viewLifecycleOwner) { yeuThichList ->
            if (yeuThichList.isNullOrEmpty()) {
                viewBinding.rvFavorite.visibility = View.GONE
                viewBinding.ivNoData.visibility = View.VISIBLE
            } else {
                viewBinding.rvFavorite.visibility = View.VISIBLE
                viewBinding.ivNoData.visibility = View.GONE
                adapter.updateData(yeuThichList.toMutableList())
                viewBinding.rvFavorite.adapter = adapter
            }
        }


    }




}