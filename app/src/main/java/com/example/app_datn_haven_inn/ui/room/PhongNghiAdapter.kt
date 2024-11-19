package com.example.app_datn_haven_inn.ui.room

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.ItemTtPhongBinding
import java.text.NumberFormat
import java.util.Locale

class PhongNghiAdapter(
    var listPhong: List<LoaiPhongModel>
) : RecyclerView.Adapter<PhongNghiAdapter.PhongNghiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhongNghiViewHolder {
        val binding = ItemTtPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhongNghiViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPhong.size
    }

    inner class PhongNghiViewHolder(val binding: ItemTtPhongBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: PhongNghiViewHolder, position: Int) {

        holder.binding.tvGiaCu.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        val phong = listPhong[position]
        holder.binding.apply {
            val imageUrl = phong.hinhAnh[0]

            if (imageUrl.isNotEmpty()) {

                Glide.with(holder.binding.root.context)
                    .load(imageUrl)
                    .into(ivPhong)
            } else {
                ivPhong.setImageResource(R.drawable.img_room1)
            }
            tvTieuDe.text = phong.tenLoaiPhong
            tvDienTich.text = phong.dienTich.toString() + " mét vuông"
            tvSLKhach.text = phong.soLuongKhach.toString() + " khách"
            tvLoaiGiuong.text = phong.giuong
            tvGiaChinhThuc.text = "${formatCurrency(phong.giaTien.toInt())}đ"

            tvTuyChinh.setOnClickListener{
                val context = holder.binding.root.context
                val intent = Intent(context, TuyChinhDatPhongActivity::class.java)
                intent.putExtra("price", phong.giaTien)
                context.startActivity(intent)
            }

        }

    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount)
    }

}