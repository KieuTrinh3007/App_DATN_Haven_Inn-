package com.example.app_datn_haven_inn.ui.room.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemSoPhongBinding

class TuyChinhDatPhongAdapter(
    var listSoPhong: List<PhongModel>,
    private val onRoomClick: (PhongModel, Boolean) -> Unit
) : RecyclerView.Adapter<TuyChinhDatPhongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSoPhong.size
    }

    fun clearSelectedRoom(){
        listSoPhong.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSoPhong.text = listSoPhong.get(position).soPhong
        val item = listSoPhong[position]
        if (listSoPhong.get(position).vip) {

            holder.binding.tvSoPhong.text = "VIP\n" + listSoPhong.get(position).soPhong
            holder.binding.tvSoPhong.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        }


        holder.binding.tvSoPhong.setBackgroundResource(
            if (item.isSelected) R.drawable.bg_room_select else R.drawable.bg_room_unselect
        )

        when (listSoPhong.get(position).trangThai) {
            0 -> { // Còn phòng
                holder.binding.root.isEnabled = true
                if (!item.isSelected) {
                    holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_unselect)
                } else {
                    holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_select)
                }
            }
            1,2 -> { // Hết phòng
                holder.binding.root.isEnabled = false
                holder.binding.tvSoPhong.setBackgroundResource(R.drawable.bg_room_selected)
            }

        }


        holder.binding.root.setOnClickListener {
            if (item.trangThai == 0) {
                item.isSelected = !item.isSelected
                notifyItemChanged(position)
                onRoomClick(item, item.isSelected)
            }
        }

    }


    class ViewHolder(val binding: ItemSoPhongBinding) : RecyclerView.ViewHolder(binding.root)

}