package com.example.app_datn_haven_inn.ui.thucDon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentThucdonBinding
import com.example.app_datn_haven_inn.ui.food.adapter.AmThucAdapter
import com.example.app_datn_haven_inn.viewModel.AmThucViewModel

class ThucDonFragment : Fragment() {

    private lateinit var binding: FragmentThucdonBinding
    private lateinit var amThucViewModel: AmThucViewModel
    private lateinit var adapter: AmThucAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThucdonBinding.inflate(inflater, container, false)

        amThucViewModel = ViewModelProvider(this).get(AmThucViewModel::class.java)

        amThucViewModel.getListamThuc()

        // Kiểm tra dữ liệu amThucList trước khi gán adapter
        amThucViewModel.amThucList.observe(viewLifecycleOwner, Observer { amThucList ->
            if (amThucList != null && amThucList.isNotEmpty()) {
                adapter = AmThucAdapter(amThucList) { amThuc ->
                    Toast.makeText(context, "Clicked: ${amThuc.tenNhaHang}", Toast.LENGTH_SHORT).show()
                }

                binding.viewThucDon.layoutManager = LinearLayoutManager(context)
                binding.viewThucDon.adapter = adapter
                binding.progressBarThucdon.visibility = View.GONE // Ẩn ProgressBar khi tải xong dữ liệu
            } else {
                Toast.makeText(context, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show()
                binding.progressBarThucdon.visibility = View.GONE
            }
        })


        // Hiển thị ProgressBar khi đang tải dữ liệu
        amThucViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                binding.progressBarThucdon.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}
