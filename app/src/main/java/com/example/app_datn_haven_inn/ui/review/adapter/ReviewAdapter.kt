package com.example.app_datn_haven_inn.ui.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.databinding.ItemReviewBinding
import com.example.app_datn_haven_inn.ui.review.model.Review

class ReviewAdapter(private val reviews: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.imgAvatar.setImageResource(review.avatarResId)
            binding.txtTennguoidung.text = review.userName
            binding.txtRating.text = "${review.rating}/10"
            binding.txtBinhluan.text = review.comment
            binding.txtNgaythang.text = review.date
        }
    }

    // Phương thức để cập nhật toàn bộ danh sách bình luận
    fun updateReviews(newReviews: List<Review>) {
        reviews.clear()
        reviews.addAll(newReviews)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size
}
