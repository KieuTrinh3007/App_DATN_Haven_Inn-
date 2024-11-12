package com.example.app_datn_haven_inn.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R

class SlideshowAdapter(private val images: List<Int>) :  RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder>() {

    class SlideshowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideshowViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_slidshow, parent, false)
        return SlideshowViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideshowViewHolder, position: Int) {
        val imageRes = images[position] ?: R.drawable.slideshow1
        holder.imageView.setImageResource(imageRes)
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size
    }