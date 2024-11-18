package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemSoPhongBinding

class TuyChinhDatPhongAdapter(
    var listSoPhong: List<PhongModel>
) : RecyclerView.Adapter<TuyChinhDatPhongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSoPhong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSoPhong.text = listSoPhong.get(position).soPhong

        when (listSoPhong.get(position).trangThai) {
            0 -> { // Hết phòng
                holder.binding.root.isEnabled = false
                holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_selected)
            }
            1 -> { // Chưa chọn
                holder.binding.root.isEnabled = true
                holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_unselect)
            }
            2 -> { // Đã chọn
                holder.binding.root.isEnabled = true
                holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_select)
            }
        }

        // Xử lý sự kiện click để chọn phòng
        holder.binding.root.setOnClickListener {
            if (listSoPhong.get(position).trangThai == 1) {
                listSoPhong.get(position).trangThai = 2
                notifyItemChanged(position)
            } else if (listSoPhong.get(position).trangThai == 2) {
                listSoPhong.get(position).trangThai = 1
                notifyItemChanged(position)
            }
        }
    }

    class ViewHolder(val binding: ItemSoPhongBinding) : RecyclerView.ViewHolder(binding.root)

}