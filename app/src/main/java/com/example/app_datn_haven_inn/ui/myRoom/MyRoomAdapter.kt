package com.example.app_datn_haven_inn.ui.myRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.databinding.ItemPhongcuatoiBinding
import java.text.NumberFormat
import java.util.*

class MyRoomAdapter(private val roomList: List<ChiTietHoaDonModel1>) :
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

        // Truy cập thông tin phòng từ `id_Phong`
        val phong = room.id_Phong

        // Gán giá trị với kiểm tra null và hiển thị thông tin phòng

        binding.roomName.text = phong.id_LoaiPhong?.tenLoaiPhong ?: "Không rõ tên phòng"
        binding.tvSoPhong.text = "Số phòng: ${phong.soPhong}"
        binding.tvVIP.text = if (phong.VIP) "VIP" else "Phòng thường"
        binding.tvSLKhach.text = "Số người: ${room.soLuongKhach ?: 0} "

        // Hiển thị giá phòng
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            .format(room.giaPhong ?: 0f)
        binding.roomPrice.text = "Giá phòng: $formattedPrice"

        // Hiển thị bữa sáng
        binding.tvBuaSang.text = if (room.buaSang == true) "Bữa sáng: Có" else "Bữa sáng: Không"

//        // Tính tổng tiền (giả sử bạn có thông tin tổng tiền ở đâu đó, nếu không, bạn có thể tính toán theo công thức của mình)
//        val totalPrice = phong.id_LoaiPhong?.giaTien?.let { it * (phong.id_LoaiPhong?.soLuongKhach ?: 1) } ?: 0f
//        val formattedTotalPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(totalPrice)
//        binding..text = "Tổng tiền: $formattedTotalPrice"

        // Hiển thị ảnh phòng
        // Giả sử bạn có dữ liệu room đã được lấy
        val imageUrl = room.id_Phong?.id_LoaiPhong?.hinhAnh?.get(0) // Lấy ảnh đầu tiên từ mảng hình ảnh
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(binding.roomImage.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.roomImage)
        } else {
            // Nếu không có ảnh, có thể sử dụng ảnh mặc định
            binding.roomImage.setImageResource(R.drawable.img_load)
        }

    }


    override fun getItemCount(): Int = roomList.size
}
