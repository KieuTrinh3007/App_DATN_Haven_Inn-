package com.example.app_datn_haven_inn.ui.room.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemChiTietGiaPhongBinding
import java.text.NumberFormat
import java.util.Locale

class SelectedRoomAdapter(
    private var selectedRooms: MutableList<PhongModel>,
    private val maxGuestCount: Int,
    private val price: Int
) : RecyclerView.Adapter<SelectedRoomAdapter.ViewHolder>() {
    var isBreakfast = false
    val guestCounts = HashMap<String, Int>()

    var onTotalPriceChanged: (() -> Unit)? = null
    var onGuestCountChanged: ((roomName: String, guestCount: Int) -> Unit)? = null
    private val hoaDonList = mutableListOf<ChiTietHoaDonModel>()

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
            holder.binding.tvGia.text = formatCurrency(roomPrice)

            // Ẩn rdoBuaSang nếu là phòng VIP
            holder.binding.tvGiaAn.visibility = View.GONE
            holder.binding.tvAnSang.visibility = View.GONE
            holder.binding.rdKhongBuaSang.visibility = View.GONE
        } else {

            val roomPrice = price
            holder.binding.tvGia.text = formatCurrency(roomPrice)

            // Hiển thị lại rdoBuaSang nếu không phải phòng VIP
            holder.binding.tvGiaAn.visibility = View.VISIBLE
            holder.binding.tvAnSang.visibility = View.VISIBLE
            holder.binding.rdKhongBuaSang.visibility = View.VISIBLE
        }


        updateBreakfastIcon(holder, room.isBreakfast)

        holder.binding.rdKhongBuaSang.setOnClickListener {
            room.isBreakfast = !room.isBreakfast
            notifyItemChanged(position)
            hoaDonList.find { it.id_Phong == room.id }?.buaSang = room.isBreakfast
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
                hoaDonList.find { it.id_Phong == room.id }?.soLuongKhach = currentCount + 1

                onTotalPriceChanged?.invoke()
                onGuestCountChanged?.invoke(roomName, guestCounts[roomName] ?: 1)
            } else {
                holder.binding.tvPlusSLKhach.alpha = 0.3f
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
                holder.binding.tvMinusSLKhach.alpha = 0.3f
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

        val hoaDon = ChiTietHoaDonModel(
            id_Phong = room.id,
            soLuongKhach = guestCounts.getOrDefault(room.soPhong, 1),
            giaPhong = if (room.vip) price + 300000.0 else price.toDouble(),
            buaSang = room.isBreakfast
        )

        Log.d("SelectedRoomAdapter", "Thêm phòng mới: $hoaDon")
        hoaDonList.add(hoaDon) // Thêm vào danh sách
        notifyItemInserted(selectedRooms.size - 1)
    }

    fun removeRoom(room: PhongModel) {
        val index = selectedRooms.indexOfFirst { it.soPhong == room.soPhong }
        if (index != -1) {
            selectedRooms.removeAt(index)
            guestCounts.remove(room.soPhong)
            notifyItemRemoved(index)
            val removedInvoice = hoaDonList.removeIf { it.id_Phong == room.id }

            onTotalPriceChanged?.invoke()
            Log.d("SelectedRoomAdapter", "Removed room: $room, Invoice removed: $removedInvoice")
        }
    }

    fun resetSelectedRooms() {
        selectedRooms.clear()
        guestCounts.clear()
        hoaDonList.clear()
        notifyDataSetChanged()
        onTotalPriceChanged?.invoke()
    }


    fun calculateTotalPrice(numberOfNights: Int): Int {
        var totalPrice = 0
        for (room in selectedRooms) {
            // Tính giá cơ bản cho phòng (VIP hoặc không VIP)
            val basePrice = if (room.vip) price + 300000 else price

            // Tính giá cho từng phòng và nhân với số đêm
            var roomPrice = basePrice * numberOfNights

            // Thêm chi phí bữa sáng nếu có
            if (room.isBreakfast) {
                roomPrice += 150000
            }

            // Cộng giá của từng phòng vào tổng giá
            totalPrice += roomPrice
        }
        return totalPrice
    }

    fun getHoaDonList(): List<ChiTietHoaDonModel> {
        return hoaDonList
    }


    fun getSelectedRooms(): List<PhongModel> {
        return selectedRooms
    }

    fun getGuestCounts(): Map<String, Int> {
        return guestCounts
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount) + " đ"
    }

    inner class ViewHolder(val binding: ItemChiTietGiaPhongBinding) :
        RecyclerView.ViewHolder(binding.root)
}
