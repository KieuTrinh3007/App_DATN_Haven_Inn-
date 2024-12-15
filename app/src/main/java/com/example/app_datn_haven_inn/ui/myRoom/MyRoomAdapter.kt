package com.example.app_datn_haven_inn.ui.myRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.ItemPhongcuatoiBinding
import java.text.NumberFormat
import java.util.*

class MyRoomAdapter(
    private val roomList: List<ChiTietHoaDonModel1>,
    private val onItemClick: (LoaiPhongModel) -> Unit
) : RecyclerView.Adapter<MyRoomAdapter.RoomViewHolder>() {

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

        // Truy cập thông tin phòng từ `id_Phong`
        val phong = room.id_Phong ?: return

        // Gán giá trị với kiểm tra null và hiển thị thông tin phòng
        binding.root.setOnClickListener {
            phong.id_LoaiPhong?.let { onItemClick(it) }
        }

        // Gán tên phòng, nếu null sẽ hiển thị "Không rõ tên phòng"
        binding.roomName.text = phong.id_LoaiPhong?.tenLoaiPhong ?: "Không rõ tên phòng"

        // Gán số phòng
        binding.tvSoPhong.text = "Số phòng: ${phong.soPhong ?: "Không xác định"}"

        // Gán VIP hoặc phòng thường
        binding.tvVIP.text = if (phong.VIP) "VIP" else "Phòng thường"

        // Gán số khách
        binding.tvSLKhach.text = "Số người: ${room.soLuongKhach ?: 0}"

        // Hiển thị giá phòng, nếu giá null sẽ hiển thị "Liên hệ"
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            .format(room.giaPhong ?: 0f)
        binding.roomPrice.text = "Giá phòng: $formattedPrice"

        // Hiển thị bữa sáng
        binding.tvBuaSang.text = if (room.buaSang == true) "Bữa sáng: Có" else "Bữa sáng: Không"

        // Hiển thị ảnh phòng
        val imageUrl = phong.id_LoaiPhong?.hinhAnh?.getOrNull(0) // Lấy ảnh đầu tiên từ mảng hình ảnh
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(binding.roomImage.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.roomImage)
        } else {
            // Nếu không có ảnh, sử dụng ảnh mặc định
            binding.roomImage.setImageResource(R.drawable.img_load)
        }
    }

    override fun getItemCount(): Int = roomList.size
}
