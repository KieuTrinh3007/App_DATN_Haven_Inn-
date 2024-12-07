package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemChiTietGiaPhongBinding

class SelectedRoomAdapter(
    private var selectedRooms: MutableList<PhongModel>,
    private val maxGuestCount: Int,
    private val price: Int
) : RecyclerView.Adapter<SelectedRoomAdapter.ViewHolder>() {
    var isBreakfast = false
    val guestCounts = HashMap<String, Int>()

    var onTotalPriceChanged: (() -> Unit)? = null
    var onGuestCountChanged: ((roomName: String, guestCount: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChiTietGiaPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return selectedRooms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val room = selectedRooms[position]
        val roomName = room.soPhong

        holder.binding.tvTenPhong.text = roomName
        if (room.vip) {
            val roomPrice = price + 300000
            holder.binding.tvGia.text = roomPrice.toString()

            // Ẩn rdoBuaSang nếu là phòng VIP
            holder.binding.tvGiaAn.visibility = View.GONE
            holder.binding.tvAnSang.visibility = View.GONE
            holder.binding.rdKhongBuaSang.visibility = View.GONE
        } else {
            val roomPrice = price
            holder.binding.tvGia.text = roomPrice.toString()

            // Hiển thị lại rdoBuaSang nếu không phải phòng VIP
            holder.binding.tvGiaAn.visibility = View.VISIBLE
            holder.binding.tvAnSang.visibility = View.VISIBLE
            holder.binding.rdKhongBuaSang.visibility = View.VISIBLE
        }




        updateBreakfastIcon(holder, room.isBreakfast)

        holder.binding.rdKhongBuaSang.setOnClickListener {
            room.isBreakfast = !room.isBreakfast
            notifyItemChanged(position)
            onTotalPriceChanged?.invoke()
            onGuestCountChanged?.invoke(roomName, guestCounts[roomName] ?: 1)

        }
        holder.binding.tvSL.text = (guestCounts[roomName] ?: 1).toString()


        // tang
        holder.binding.tvPlusSLKhach.setOnClickListener {
            val currentCount = guestCounts[roomName] ?: 1
            if (currentCount < maxGuestCount) {
                guestCounts[roomName] = currentCount + 1
                holder.binding.tvSL.text = (currentCount + 1).toString()
                onTotalPriceChanged?.invoke()
                onGuestCountChanged?.invoke(roomName, guestCounts[roomName] ?: 1)
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "Số khách tối đa là $maxGuestCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

// giam
        holder.binding.tvMinusSLKhach.setOnClickListener {
            val currentCount = guestCounts[roomName] ?: 1
            if (currentCount > 1) {
                guestCounts[roomName] = currentCount - 1
                holder.binding.tvSL.text = (currentCount - 1).toString()
                onTotalPriceChanged?.invoke()
                onGuestCountChanged?.invoke(roomName, guestCounts[roomName] ?: 1)
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "Số khách tối thiểu là 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }



    private fun updateBreakfastIcon(holder: ViewHolder,isBreakfast: Boolean) {
        if (isBreakfast) {
            holder.binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_breakfast)
        } else {
            holder.binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_no_breakfast)
        }
    }

    fun addRoom(room: PhongModel) {
        selectedRooms.add(room)
        guestCounts[room.soPhong] = 1
        notifyItemInserted(selectedRooms.size - 1)
        onTotalPriceChanged?.invoke()
    }

    fun removeRoom(room: PhongModel) {
        val index = selectedRooms.indexOfFirst { it.soPhong == room.soPhong }
        if (index != -1) {
            selectedRooms.removeAt(index)
            guestCounts.remove(room.soPhong)
            notifyItemRemoved(index)
            onTotalPriceChanged?.invoke()
        }
    }


    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (room in selectedRooms) {
            // Tính giá cơ bản cho phòng (VIP hoặc không VIP)
            val basePrice = if (room.vip) price + 300000 else price

            // Cộng giá phòng vào tổng giá
            totalPrice += basePrice

            // Thêm chi phí bữa sáng nếu có
            if (room.isBreakfast) {
                totalPrice += 150000
            }
        }
        return totalPrice
    }

    fun getSelectedRooms(): List<PhongModel> {
        return selectedRooms
    }

    fun getGuestCounts(): Map<String, Int> {
        return guestCounts
    }




    inner class ViewHolder(val binding: ItemChiTietGiaPhongBinding) :
        RecyclerView.ViewHolder(binding.root)
}
