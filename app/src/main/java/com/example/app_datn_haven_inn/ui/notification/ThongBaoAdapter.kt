package com.example.app_datn_haven_inn.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.databinding.ItemThongbaoBinding

class ThongBaoAdapter(
    var thongBaoList: MutableList<ThongBaoModel>,
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder>() {

    class ThongBaoViewHolder(val binding: ItemThongbaoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThongBaoViewHolder {
        val binding = ItemThongbaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThongBaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThongBaoViewHolder, position: Int) {
        val thongBao = thongBaoList[position]

        with(holder.binding) {
            tvTieuDe.text = thongBao.tieuDe
            tvNoiDung.text = thongBao.noiDung
            tvNgayGui.text = thongBao.ngayGui

            // Đổi màu nền dựa trên trạng thái của thông báo
            root.setBackgroundResource(
                if (!thongBao.trangThai) R.drawable.item_border_active
                else R.drawable.item_border_inactive
            )

            root.setOnClickListener {
                thongBao.trangThai = true // Đánh dấu thông báo là đã đọc
                notifyItemChanged(position) // Cập nhật chỉ mục trong RecyclerView
                onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = thongBaoList.size

    /**
     * Cập nhật danh sách thông báo mới
     */
    fun updateList(newList: List<ThongBaoModel>) {
        thongBaoList.clear()
        thongBaoList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<ThongBaoModel> {
        return thongBaoList
    }

}
