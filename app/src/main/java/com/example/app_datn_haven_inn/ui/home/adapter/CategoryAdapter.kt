package com.example.app_datn_haven_inn.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ItemRvCategoryBinding
import com.example.app_datn_haven_inn.ui.home.OnClickItem

class CategoryAdapter(private val categories: List<String>,
                      private val onItemClick: OnClickItem
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0



    inner class CategoryViewHolder(val binding: ItemRvCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemRvCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.tvCategoryName.text = categories[position]


        if (position == selectedPosition) {
            holder.binding.tvCategoryName.setTextColor(Color.WHITE)
            holder.binding.tvCategoryName.setBackgroundResource(R.drawable.custom_bg_category_selected_t)



        } else {
            holder.binding.tvCategoryName.setTextColor(Color.BLACK)
            holder.binding.tvCategoryName.setBackgroundResource(R.drawable.custom_bg_category_unselect_t)

        }


        holder.itemView.setOnClickListener {
            selectedPosition = position
            onItemClick.onClickItem(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = categories.size
}
