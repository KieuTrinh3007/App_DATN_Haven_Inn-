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
    private val onItemClick: (LoaiPhongModel) -> Unit
) : RecyclerView.Adapter<RoomTopAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgRoom: ImageView = itemView.findViewById(R.id.imgImageRoomTop)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitleRoomTop)
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

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick(room)
        }
    }

    override fun getItemCount(): Int {
        return roomList.size.coerceAtMost(4)
    }
}

