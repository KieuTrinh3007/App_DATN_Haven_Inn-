package com.example.app_datn_haven_inn.ui.room.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DanhGiaAdapter(private val context: Context, var listReview: List<DanhGiaModel>) : RecyclerView.Adapter<DanhGiaAdapter.ReviewViewHolder>() {
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService<NguoiDungService>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // Inflate layout item_danhgia.xml
        val view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = listReview[position]

        // Hiển thị các thông tin đánh giá
        holder.tvNhanXet.text = review.binhLuan
        holder.ratingBarDanhGia.text = review.soDiem.toString()
        holder.tvNgayDanhGia.text = review.ngayDanhGia

        // Lấy thông tin người dùng từ API bằng ID người dùng
        getUserInfoAndSetUpReview(holder, review.id_NguoiDung)
    }

    override fun getItemCount(): Int = listReview.size

    private fun getUserInfoAndSetUpReview(holder: ReviewViewHolder, userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Gọi API lấy thông tin người dùng theo id
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == userId }
                    user?.let {
                        // Cập nhật giao diện với tên và ảnh người dùng
                        withContext(Dispatchers.Main) {
                            holder.tvTenNguoiDungDanhGia.text = it.tenNguoiDung
                            if (!it.hinhAnh.isNullOrEmpty()) {
                                Glide.with(context)
                                    .load(it.hinhAnh)
                                    .circleCrop()
                                    .into(holder.imgAvatarDanhGia)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ReviewAdapter", "Error fetching user data: ${e.message}")
            }
        }
    }

    // ViewHolder cho review
    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatarDanhGia: ImageView = view.findViewById(R.id.img_avatar_danhgia)
        val tvTenNguoiDungDanhGia: TextView = view.findViewById(R.id.tv_ten_nguoi_dung_danhgia)
        val tvNhanXet: TextView = view.findViewById(R.id.tv_nhan_xet)
        val ratingBarDanhGia: TextView = view.findViewById(R.id.rating_bar_danhgia)
        val tvNgayDanhGia: TextView = view.findViewById(R.id.tv_ngay_danhgia)
    }
}
