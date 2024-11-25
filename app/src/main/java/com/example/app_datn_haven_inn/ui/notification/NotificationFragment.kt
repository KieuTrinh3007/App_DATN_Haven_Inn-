package com.example.app_datn_haven_inn.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.viewModel.ThongBaoViewModel
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.ui.notification.ThongBaoAdapter

class NotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var thongBaoAdapter: ThongBaoAdapter
    private lateinit var thongBaoViewModel: ThongBaoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewThongBao)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        thongBaoViewModel = ViewModelProvider(this)[ThongBaoViewModel::class.java]

        thongBaoViewModel.thongBaoList.observe(viewLifecycleOwner) { thongBaoList ->
            thongBaoList?.let {
                thongBaoAdapter = ThongBaoAdapter(it) { position ->
                    val updatedThongBao = it[position].copy(trangThai = true) // Cập nhật trạng thái
                    thongBaoViewModel.updatethongBao(it[position].id, updatedThongBao)
                    thongBaoAdapter.updateList(it.toMutableList())
                }
                recyclerView.adapter = thongBaoAdapter
            }
        }

        thongBaoViewModel.getListthongBao()

        return view
    }
}
