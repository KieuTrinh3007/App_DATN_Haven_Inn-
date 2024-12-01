package com.example.app_datn_haven_inn.ui.room.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.databinding.ItemTtPhongBinding
import com.example.app_datn_haven_inn.ui.room.RoomDetailActivity
import com.example.app_datn_haven_inn.ui.room.TuyChinhDatPhongActivity
import com.example.app_datn_haven_inn.utils.SharePrefUtils.loadFavoriteState
import com.example.app_datn_haven_inn.utils.SharePrefUtils.saveFavoriteState
import java.text.NumberFormat
import java.util.Locale

class PhongNghiAdapter(
    var listPhong: List<LoaiPhongModel>,
    val context : Context
) : RecyclerView.Adapter<PhongNghiAdapter.PhongNghiViewHolder>() {
    private var onItemClick: ((LoaiPhongModel, Int) -> Unit)? = null
    private var onFavotiteSelected: ((LoaiPhongModel) -> Unit)? = null

    fun setonFavotiteSelected(onFavotiteSelected: (LoaiPhongModel) -> Unit) {
        this.onFavotiteSelected = onFavotiteSelected
    }

    fun setOnItemClickListener(onItemClick: (LoaiPhongModel, Int) -> Unit) {
        this.onItemClick = onItemClick

    }

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

        val phong = listPhong[position]
        phong.isFavorite = loadFavoriteState(context, phong)
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
            tvGiaVip.text = "${formatCurrency(phong.giaTien.toInt() + 300000)}đ"


            tvTuyChinh.setOnClickListener{
                val context = holder.binding.root.context
                val intent = Intent(context, TuyChinhDatPhongActivity::class.java)
                intent.putExtra("id_LoaiPhong", phong.id)
                context.startActivity(intent)
            }


            holder.itemView.setOnClickListener {
                Log.d("hinhAnh",phong.hinhAnh.toString())
                val intent = Intent(context, RoomDetailActivity::class.java)
                intent.putExtra("id_LoaiPhong", phong.id)
                intent.putExtra("tenLoaiPhong", phong.tenLoaiPhong)
                intent.putExtra("giuong", phong.giuong)
                intent.putExtra("soLuongKhach", phong.soLuongKhach.toString())
                intent.putExtra("dienTich", phong.dienTich.toString())
                intent.putExtra("hinhAnh", phong.hinhAnh.toTypedArray())
                intent.putExtra("moTa", phong.moTa)
                intent.putExtra("isFavorite", phong.isFavorite)
                context.startActivity(intent)
            }

            updateFavoriteIcon(this, phong)

            ivFavorite.setOnClickListener {
                phong.isFavorite = !(phong.isFavorite ?: false)
                onFavotiteSelected?.invoke(phong)
                saveFavoriteState(context, phong)
                updateFavoriteIcon(this, phong)
            }

        }

    }

    fun updateList(newList: List<LoaiPhongModel>) {
        listPhong = newList
        notifyDataSetChanged()
    }
     fun updateFavoriteIcon(binding: ItemTtPhongBinding, phong: LoaiPhongModel) {
        binding.ivFavorite.setImageResource(
            if (phong.isFavorite == true) R.drawable.ic_favorite_select
            else R.drawable.ic_favorite_unselect
        )
    }
    fun updateFavoriteState(itemId: String, isFavorite: Boolean) {
        val index = listPhong.indexOfFirst { it.id == itemId }
        if (index != -1) {
            listPhong[index].isFavorite = isFavorite
            notifyItemChanged(index)
        }
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount)
    }

}