package com.example.app_datn_haven_inn.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ItemRvCategoryBinding
import com.example.app_datn_haven_inn.ui.home.OnClickItem

class CategoryAdapter(
    private val categories: List<String>,
    private val onItemClick: OnClickItem
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    inner class CategoryViewHolder(val binding: ItemRvCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvCategoryName.text = categories[position]

            // Set màu nền và màu chữ theo trạng thái chọn
            if (position == selectedPosition) {
                binding.tvCategoryName.setTextColor(Color.WHITE)
                binding.tvCategoryName.setBackgroundResource(R.drawable.custom_bg_category_selected_t)
            } else {
                binding.tvCategoryName.setTextColor(Color.BLACK)
                binding.tvCategoryName.setBackgroundResource(R.drawable.custom_bg_category_unselect_t)
            }

            // Sự kiện khi click vào item
            itemView.setOnClickListener {
                selectedPosition = position
                onItemClick.onClickItem(position)
                notifyDataSetChanged() // Cập nhật RecyclerView để làm mới trạng thái
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemRvCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = categories.size
}
