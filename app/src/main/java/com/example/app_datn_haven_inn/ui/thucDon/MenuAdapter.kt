package com.example.app_datn_haven_inn.ui.thucDon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ItemMenuBinding

class MenuAdapter(private val menuList: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewMenuItem)

        fun bind(menuItem: String) {
            Glide.with(itemView.context)
                .load(menuItem)
                .placeholder(R.drawable.img_20)
                .error(R.drawable.bg_radius_5_yellow)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(menuItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.bind(menuItem)
    }

    override fun getItemCount(): Int = menuList.size
}
