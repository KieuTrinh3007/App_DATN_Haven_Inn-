package com.example.app_datn_haven_inn.ui.myRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemPhongcuatoiBinding

class MyRoomAdapter(private val roomList: List<PhongModel>) :
    RecyclerView.Adapter<MyRoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(val binding: ItemPhongcuatoiBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemPhongcuatoiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        val binding = holder.binding

        // Kiểm tra null và cung cấp giá trị mặc định
        binding.roomName.text = room.loaiPhong?.tenLoaiPhong ?: "Tên phòng không có"
        binding.tvQuangCanhPct.text = room.loaiPhong?.moTa ?: "Mô tả không có"
        binding.tvSLKhachPct.text = "${room.loaiPhong?.soLuongKhach ?: 0} Khách"
        binding.tvLoaiGiuongPct.text = room.loaiPhong?.giuong ?: "Không có thông tin giường"
        binding.tvDienTichPct.text = "${room.loaiPhong?.dienTich ?: 0f} mét vuông"
        binding.roomPrice.text = "Tổng tiền: ${room.loaiPhong?.giaTien ?: 0f} đ"

        // Load hình ảnh với kiểm tra null
        Glide.with(binding.root.context)
            .load(room.loaiPhong?.hinhAnh?.firstOrNull())  // Lấy hình ảnh đầu tiên hoặc null nếu không có
            .into(binding.imgPct)
    }


    override fun getItemCount(): Int = roomList.size
}
