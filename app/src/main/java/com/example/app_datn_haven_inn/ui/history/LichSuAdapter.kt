package com.example.app_datn_haven_inn.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.databinding.ItemLichsuBinding
import com.squareup.picasso.Picasso

class LichSuAdapter(private val historyList: List<HoaDonModel>) :
    RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder>() {

    class LichSuViewHolder(val binding: ItemLichsuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LichSuViewHolder {
        val binding = ItemLichsuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LichSuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LichSuViewHolder, position: Int) {
        val hoaDon = historyList[position]
        val binding = holder.binding

        // Kiểm tra chiTiet không phải null và không rỗng
        val chitiet = hoaDon.chiTiet.takeIf { it.isNotEmpty() } ?: ArrayList<ChiTietHoaDonModel1>()

        // Hiển thị thông tin từng chi tiết
        val tenloaiList = chitiet.joinToString { it.tenLoaiPhong ?: "Không có tên phòng" }
        binding.txtTenPhong.text = "Tên loại phòng: $tenloaiList"

        val soPhongList = chitiet.joinToString { it.soPhong ?: "Không có số phòng" }
        binding.txtSoPhong.text = "Phòng: $soPhongList"
        binding.txtNgayNhanPhong.text = "Ngày nhận phòng: ${hoaDon.ngayNhanPhong}"

        // Hiển thị tổng tiền
        val tongTien = chitiet.sumOf { it.tongTien }
        binding.txtTongTien.text = "Tổng tiền: $tongTien"

        // Hiển thị ảnh phòng (chỉ lấy ảnh của phòng đầu tiên trong danh sách chi tiết)
        val firstRoom = chitiet.firstOrNull()
        if (firstRoom != null && firstRoom.hinhAnh.isNotEmpty()) {
            Picasso.get().load(firstRoom.hinhAnh.first()).into(binding.imgPhong)
        }

        // Xử lý nút hành động
        binding.btnAction.setOnClickListener {
            // Xử lý hành động đặt lại, đánh giá, v.v.
            // Ví dụ: chuyển hướng đến một màn hình khác
        }
    }

    override fun getItemCount(): Int = historyList.size
}
