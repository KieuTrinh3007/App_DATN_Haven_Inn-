package com.example.app_datn_haven_inn.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.FragmentFavoriteBinding
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var viewModel: YeuThichViewModel
    private var adapter = FavoriteAdapter(
       mutableListOf(),
        onFavoriteClick = { loaiPhong, position ->
        }
    )


    override fun inflateViewBinding(): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesByUserId(idUser = "6724a13a2378017ace035c51")
        viewModel.yeuThichList1.observe(viewLifecycleOwner) { yeuThichList ->
            yeuThichList?.let {
                adapter.updateData(it.toMutableList())
                viewBinding.rvFavorite.adapter = adapter
            }

        }
    }

    override fun initView() {
        super.initView()

        viewModel = ViewModelProvider(this)[YeuThichViewModel::class.java]
        viewModel.getFavoritesByUserId(idUser = "6724a13a2378017ace035c51")



        viewModel.yeuThichList1.observe(viewLifecycleOwner) { yeuThichList ->
            yeuThichList?.let {
                adapter.updateData(it.toMutableList())
                viewBinding.rvFavorite.adapter = adapter
            }

        }


    }


}