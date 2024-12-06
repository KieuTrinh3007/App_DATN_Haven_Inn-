package com.example.app_datn_haven_inn.ui.notification

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.Socket.SocketHandler
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.databinding.FragmentNotificationBinding
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.example.app_datn_haven_inn.viewModel.ThongBaoViewModel
import org.json.JSONObject

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

        // Kết nối socket
        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        // Lắng nghe sự kiện từ Socket.IO
        SocketHandler.onEvent("new-notification") { data ->
            val tieuDe = data.getString("tieuDe")
            val noiDung = data.getString("noiDung")
            val userId = SharedPrefsHelper.getIdNguoiDung(requireContext())
            val ngayGui = "2024-12-04"
            val trangThai = false
            // Cập nhật giao diện người dùng sau khi nhận thông báo mới
            requireActivity().runOnUiThread {
                thongBaoAdapter.updateList(listOf(ThongBaoModel(userId!!,tieuDe, noiDung, ngayGui, trangThai))) // Cập nhật RecyclerView
                playNotificationSound() // Phát âm thanh thông báo
            }
        }
    }

    private fun playNotificationSound() {
        val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mediaPlayer = MediaPlayer.create(requireContext(), notificationUri)
        mediaPlayer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        SocketHandler.closeConnection() // Ngắt kết nối socket khi view bị hủy
    }
}
