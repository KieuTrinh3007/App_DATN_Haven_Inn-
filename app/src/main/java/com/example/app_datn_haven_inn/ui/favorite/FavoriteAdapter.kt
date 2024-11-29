package com.example.app_datn_haven_inn.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.ItemTtPhongBinding
import java.text.NumberFormat
import java.util.Locale

class FavoriteAdapter(
    private var listFavorite: MutableList<LoaiPhongModel>,
    private val onFavoriteClick: (LoaiPhongModel, Int) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(val binding: ItemTtPhongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemTtPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val loaiPhong = listFavorite[position]
        holder.binding.apply {

            val imageUrl = loaiPhong.hinhAnh[0]

            if (imageUrl.isNotEmpty()) {

                Glide.with(holder.binding.root.context)
                    .load(imageUrl)
                    .into(ivPhong)
            } else {
                ivPhong.setImageResource(R.drawable.img_room1)
            }
            tvTieuDe.text = loaiPhong.tenLoaiPhong
            tvDienTich.text = loaiPhong.dienTich.toString() + " mét vuông"
            tvSLKhach.text = loaiPhong.soLuongKhach.toString() + " khách"
            tvLoaiGiuong.text = loaiPhong.giuong
            tvGiaChinhThuc.text = "${formatCurrency(loaiPhong.giaTien.toInt())}đ"

            ivFavorite.setImageResource(R.drawable.ic_favorite_select)

            ivFavorite.setOnClickListener {
                onFavoriteClick(loaiPhong, position)
            }
        }
    }


    fun updateData(newList: MutableList<LoaiPhongModel>) {
        listFavorite = newList
        notifyDataSetChanged()
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount)
    }

}


