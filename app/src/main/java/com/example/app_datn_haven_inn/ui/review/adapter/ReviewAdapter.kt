package com.example.app_datn_haven_inn.ui.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.DanhGiaNguoiDungModel
import com.example.app_datn_haven_inn.databinding.ItemReviewBinding

class ReviewAdapter(
    var listReview: List<DanhGiaNguoiDungModel>
): RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return listReview.size
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.binding.txtTennguoidung.text = listReview[position].tenNguoiDung
        holder.binding.txtBinhluan.text = listReview[position].binhLuan
        holder.binding.txtRating.text = listReview[position].soDiem.toString()
        Glide.with(holder.binding.root.context)
            .load(listReview[position].hinhAnh)
            .into(holder.binding.imgAvatar)

        val dateOnly = listReview[position].ngayDanhGia.substring(0, 10)
        holder.binding.txtNgaythang.text = dateOnly
    }

}
