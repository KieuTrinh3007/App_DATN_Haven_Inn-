package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ItemSoPhongBinding

class TuyChinhDatPhongAdapter(
    private var listSoPhong: List<PhongModel>
) : RecyclerView.Adapter<TuyChinhDatPhongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSoPhong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSoPhong.text = listSoPhong.get(position).soPhong
    }

    class ViewHolder(val binding: ItemSoPhongBinding) : RecyclerView.ViewHolder(binding.root)

}