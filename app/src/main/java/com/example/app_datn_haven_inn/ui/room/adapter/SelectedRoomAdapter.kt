package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ItemChiTietGiaPhongBinding

class SelectedRoomAdapter(
    private var selectedRooms: MutableList<Pair<String, String>>
) : RecyclerView.Adapter<SelectedRoomAdapter.ViewHolder>() {
    var isBreakfast = false
    private val guestCounts = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChiTietGiaPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)


    }

    override fun getItemCount(): Int {
        return selectedRooms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = selectedRooms[position]
        val roomName = room.first
        val roomPrice = room.second
        holder.binding.tvTenPhong.text = roomName
        holder.binding.tvGia.text = roomPrice

        updateBreakfastIcon(holder)

        holder.binding.rdKhongBuaSang.setOnClickListener {
            isBreakfast = !isBreakfast
            notifyItemChanged(position)
        }

        val currentGuestCount = guestCounts[roomName] ?: 1
        holder.binding.tvSL.text = currentGuestCount.toString()


        holder.binding.tvPlusSLKhach.setOnClickListener {
            val newCount = currentGuestCount + 1
            guestCounts[roomName] = newCount
            holder.binding.tvSL.text = newCount.toString()
        }


        holder.binding.tvMinusSLKhach.setOnClickListener {
            if (currentGuestCount > 1) {
                val newCount = currentGuestCount - 1
                guestCounts[roomName] = newCount
                holder.binding.tvSL.text = newCount.toString()
            }
        }
    }

    private fun updateBreakfastIcon(holder: ViewHolder) {
        if (isBreakfast) {
            holder.binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_breakfast)
        } else {
            holder.binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_no_breakfast)
        }
    }

    fun addRoom(room: String, price: String) {
        selectedRooms.add(Pair(room, price))
        guestCounts[room] = 1
        notifyItemInserted(selectedRooms.size - 1)
    }

    fun removeRoom(room: String) {
        val index = selectedRooms.indexOfFirst { it.first == room }
        if (index != -1) {
            selectedRooms.removeAt(index)
            guestCounts.remove(room)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(val binding: ItemChiTietGiaPhongBinding) :
        RecyclerView.ViewHolder(binding.root)
}
