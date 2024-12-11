package com.example.app_datn_haven_inn.ui.food.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.databinding.ItemThucdonBinding
import com.example.app_datn_haven_inn.dialog.DialogSignIn
import com.example.app_datn_haven_inn.ui.thucDon.KhamPhaThucDon
import com.example.app_datn_haven_inn.utils.SharePrefUtils

class AmThucAdapter(
    private val listAmThuc: List<AmThucModel>,
    private val onButtonClick: (AmThucModel) -> Unit
) : RecyclerView.Adapter<AmThucAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemThucdonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listAmThuc.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listAmThuc[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemThucdonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AmThucModel) {
            binding.tvTitleThucDon.text = item.tenNhaHang
            binding.tvDescriptionThucDon.text = item.moTa
            binding.tvLocationThucDon.text = "Vị trí: ${item.viTri}"
            binding.tvHoursThucDon.text = "Giờ hoạt động: ${item.gioMoCua} - ${item.gioDongCua}"
            binding.tvPhoneThucDon.text = "Hotline: ${item.hotline}"

            Glide.with(binding.imgThucDon.context)
                .load(item.hinhAnh)
                .placeholder(R.drawable.avat2)
                .error(R.drawable.iv_dot_off)
                .into(binding.imgThucDon)

            binding.btnThucDon.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, KhamPhaThucDon::class.java)
                intent.putExtra("amThuc", item)
                context.startActivity(intent)
            }

        }
    }
}
