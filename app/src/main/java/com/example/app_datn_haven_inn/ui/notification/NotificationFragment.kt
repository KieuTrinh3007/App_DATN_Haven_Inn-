package com.example.app_datn_haven_inn.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.Socket.SocketHandler
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

        // Khởi tạo ViewModel
        thongBaoViewModel = ViewModelProvider(this)[ThongBaoViewModel::class.java]

        // Cấu hình adapter cho RecyclerView
        thongBaoAdapter = ThongBaoAdapter(mutableListOf(), { position ->
            val thongBao = thongBaoAdapter.thongBaoList[position]
            thongBaoViewModel.updatethongBao(thongBao.id) // Cập nhật trạng thái từ server
        }, thongBaoViewModel)

        binding.recyclerViewThongBao.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = thongBaoAdapter
        }

        // Quan sát danh sách thông báo
        thongBaoViewModel.thongBaoList.observe(viewLifecycleOwner) { thongBaoList ->
            thongBaoList?.let {
                val userId = SharedPrefsHelper.getIdNguoiDung(requireContext())
                if (userId != null) {
                    val filteredList = it.filter { thongBao -> thongBao.id_NguoiDung == userId }
                    thongBaoAdapter.updateList(filteredList.toMutableList())
                    if (filteredList.isEmpty()) {
                        binding.tvNoData.visibility = View.VISIBLE
                        binding.recyclerViewThongBao.visibility = View.GONE
                    } else {
                        binding.tvNoData.visibility = View.GONE
                        binding.recyclerViewThongBao.visibility = View.VISIBLE
                    }
                } else {
                    thongBaoAdapter.updateList(mutableListOf())
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.recyclerViewThongBao.visibility = View.GONE
                }
            }
        }

        // Quan sát trạng thái loading
        thongBaoViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Lấy danh sách thông báo
        thongBaoViewModel.getListthongBao()

        // Kết nối socket
        val userId = SharedPrefsHelper.getIdNguoiDung(requireContext())
        SocketHandler.setSocket(userId)
        SocketHandler.establishConnection()

        // Lắng nghe sự kiện từ socket
        SocketHandler.onEvent("new-notification") { data ->
            val idNguoiDungFromSocket = data.getString("id_NguoiDung")
            val message = data.getString("message")

            if (idNguoiDungFromSocket == userId) {
                requireActivity().runOnUiThread {
                    showNotification("Thông báo mới", message)
                    thongBaoViewModel.getListthongBao()
                    thongBaoAdapter.notifyDataSetChanged()
                    playNotificationSound()
                }
            }
        }
    }

    private fun playNotificationSound() {
        val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mediaPlayer = MediaPlayer.create(requireContext(), notificationUri)
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        mediaPlayer.start()
    }

    private fun showNotification(tieuDe: String, noiDung: String) {
        NotificationUtils.createNotificationChannel(requireContext())

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(requireContext(), "notification_channel_id")
            .setContentTitle(tieuDe)
            .setContentText(noiDung)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        SocketHandler.closeConnection()
    }
}

