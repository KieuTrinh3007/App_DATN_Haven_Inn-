package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.database.model.TienNghiPhongChiTietModel
import com.example.app_datn_haven_inn.databinding.ItemTiennghiphongBinding

class TienNghiPhongAdapter(var items: List<TienNghiPhongChiTietModel>) : RecyclerView.Adapter<TienNghiPhongAdapter.TienNghiViewHolder>() {

    class TienNghiViewHolder(var binding: ItemTiennghiphongBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TienNghiViewHolder {
        val item = ItemTiennghiphongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TienNghiViewHolder(item)
    }

    override fun onBindViewHolder(holder: TienNghiViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvMoTa.text = item.moTa
        holder.binding.tvTenTienNghi.text = item.id_TienNghi.tenTienNghi
        // Load image
        Glide.with(holder.binding.root.context)
            .load(item.id_TienNghi.image)
            .into(holder.binding.imgTienNghi)

    }

    override fun getItemCount(): Int = items.size
}
