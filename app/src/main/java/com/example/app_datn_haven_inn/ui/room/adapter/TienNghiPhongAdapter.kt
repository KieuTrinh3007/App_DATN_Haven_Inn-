package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.room.model.TienNghiPhong

class TienNghiPhongAdapter(private val items: List<TienNghiPhong>) : RecyclerView.Adapter<TienNghiPhongAdapter.TienNghiViewHolder>() {

    class TienNghiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgTienNghi: ImageView = view.findViewById(R.id.imgTienNghi)
        val tvTenTienNghi: TextView = view.findViewById(R.id.tvTenTienNghi)
        val tvMoTa: TextView = view.findViewById(R.id.tvMoTa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TienNghiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tiennghiphong, parent, false)
        return TienNghiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TienNghiViewHolder, position: Int) {
        val tienNghi = items[position]
        holder.tvTenTienNghi.text = tienNghi.tenTienNghi
        holder.tvMoTa.text = tienNghi.moTa
        holder.imgTienNghi.setImageResource(tienNghi.imageResId) // Assuming image is a drawable resource ID
    }

    override fun getItemCount(): Int = items.size
}
