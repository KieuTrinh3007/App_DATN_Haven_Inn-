package com.example.app_datn_haven_inn.ui.room.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.databinding.ItemPhongBinding
import com.example.app_datn_haven_inn.ui.home.OnClickItem

class CategoryPhongAdapter (
    private val categories: List<String>,
    private val onItemClick: OnClickItem
): RecyclerView.Adapter<CategoryPhongAdapter.CategoryPhongViewHolder>(){
    private var selectedPosition = 0



    inner class CategoryPhongViewHolder(val binding: ItemPhongBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryPhongViewHolder {
        val binding = ItemPhongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryPhongViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryPhongViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.tvCategoryName.text = categories[position]


        if (position == selectedPosition) {
            holder.binding.tvCategoryName.setTextColor(Color.WHITE)

        } else {
            holder.binding.tvCategoryName.setTextColor(Color.BLACK)

        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            onItemClick.onClickItem(position)
            notifyDataSetChanged()
        }
    }
}