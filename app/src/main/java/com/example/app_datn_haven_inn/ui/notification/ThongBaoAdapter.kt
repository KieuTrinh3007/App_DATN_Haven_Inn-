package com.example.app_datn_haven_inn.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.databinding.ItemThongbaoBinding

class ThongBaoAdapter(
    private var thongBaoList: List<ThongBaoModel>,
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

            if (!thongBao.trangThai) {
                root.setBackgroundResource(R.drawable.item_border_active) // Đảm bảo item_border_active có màu nền xanh
            } else {
                root.setBackgroundResource(R.drawable.item_border_inactive)
            }

            root.setOnClickListener {
                thongBao.trangThai = true
                onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = thongBaoList.size

    fun updateList(newList: List<ThongBaoModel>) {
        thongBaoList = newList
        notifyDataSetChanged()
    }
}
