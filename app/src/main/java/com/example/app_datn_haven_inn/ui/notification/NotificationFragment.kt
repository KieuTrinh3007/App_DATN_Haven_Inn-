package com.example.app_datn_haven_inn.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.databinding.FragmentNotificationBinding
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.example.app_datn_haven_inn.viewModel.ThongBaoViewModel

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var thongBaoViewModel: ThongBaoViewModel
    private lateinit var thongBaoAdapter: ThongBaoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thongBaoViewModel = ViewModelProvider(this)[ThongBaoViewModel::class.java]

        thongBaoAdapter = ThongBaoAdapter(emptyList()) { position ->
            // Xử lý sự kiện click vào item
        }
        binding.recyclerViewThongBao.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = thongBaoAdapter
        }

        thongBaoViewModel.thongBaoList.observe(viewLifecycleOwner) { thongBaoList ->
            thongBaoList?.let {
                val userId = SharedPrefsHelper.getIdNguoiDung(requireContext())
                if (userId != null) {
                    val filteredList = it.filter { thongBao -> thongBao.id_NguoiDung == userId }
                    thongBaoAdapter.updateList(filteredList)

                    binding.tvNoData.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    thongBaoAdapter.updateList(emptyList())
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }
        }

        thongBaoViewModel.getListthongBao()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
