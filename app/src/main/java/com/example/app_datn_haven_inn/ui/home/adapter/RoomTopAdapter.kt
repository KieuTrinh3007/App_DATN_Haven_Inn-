package com.example.app_datn_haven_inn.ui.home.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel

class RoomTopAdapter(
    private val context: Context,
    private val roomList: List<LoaiPhongModel>,
    private val danhGiaMap: Map<String, Pair<Double, Int>>, // Thêm map lưu số điểm và số lượng
    private val onItemClick: (LoaiPhongModel) -> Unit
) : RecyclerView.Adapter<RoomTopAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgRoom: ImageView = itemView.findViewById(R.id.imgImageRoomTop)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitleRoomTop)
        val txtDanhGia: TextView = itemView.findViewById(R.id.txt_soDiem_nhanXet)
        val txtTrangThai: TextView = itemView.findViewById(R.id.txt_danhGia_trangthai)
        val txtSoNhanXet: TextView = itemView.findViewById(R.id.txt_soLuongNhanXet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_room_top, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.txtTitle.text = room.tenLoaiPhong

        // Load hình ảnh đầu tiên
        if (room.hinhAnh.isNotEmpty()) {
            Glide.with(context)
                .load(room.hinhAnh[0])
                .into(holder.imgRoom)
        }

        // Lấy thông tin đánh giá từ map
        val (soDiem, soLuongDanhGia) = danhGiaMap[room.id] ?: Pair(0.0, 0)

        // Hiển thị số điểm và số lượng đánh giá
        holder.txtDanhGia.text = "$soDiem"
        holder.txtSoNhanXet.text = "$soLuongDanhGia nhận xét"

        val ratingStatus = when {
            soDiem >= 9.0 -> "Tuyệt vời"
            soDiem >= 7.0 -> "Tốt"
            soDiem >= 5.0 -> "Bình thường"
            soDiem >= 3.0 -> "Tệ"
            else -> "Rất tệ"
        }

        // Hiển thị trạng thái nếu có điểm đánh giá
        if (soDiem > 0) {
            holder.txtTrangThai.text = ratingStatus
            holder.txtTrangThai.visibility = View.VISIBLE
        } else {
            holder.txtTrangThai.visibility = View.GONE
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick(room)
        }
    }

    override fun getItemCount(): Int {
        return roomList.size.coerceAtMost(4)
    }
}


