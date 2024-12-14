package com.example.app_datn_haven_inn.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.AmThucModel

class AmThucAdapter(private val context: Context, private val amThucList: List<AmThucModel>) :
    RecyclerView.Adapter<AmThucAdapter.AmThucViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmThucViewHolder {
        // Inflate item layout
        val view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false)
        return AmThucViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmThucViewHolder, position: Int) {
        // Get current item in the list
        val amThuc = amThucList[position]

        // Load image using Glide
        Glide.with(context)
            .load(amThuc.hinhAnh)  // Image URL from AmThucModel
            .placeholder(R.drawable.img_20) // Default image while loading
            .error(R.drawable.img_20) // Image to show if loading fails
            .into(holder.imgFood)
    }

    override fun getItemCount(): Int {
        return amThucList.size
    }

    // ViewHolder class to hold references to views
    class AmThucViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFood: ImageView = itemView.findViewById(R.id.imageViewFood)
    }
}
