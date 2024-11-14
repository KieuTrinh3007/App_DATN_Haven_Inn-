package com.example.app_datn_haven_inn.ui.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R

class TienNghiNoiBatAdapter(private val items: List<String>) : RecyclerView.Adapter<TienNghiNoiBatAdapter.TienNghiNoiBatViewHolder>() {

    class TienNghiNoiBatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv_tiennghinoibat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TienNghiNoiBatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tiennghinoibat, parent, false)
        return TienNghiNoiBatViewHolder(view)
    }

    override fun onBindViewHolder(holder: TienNghiNoiBatViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
