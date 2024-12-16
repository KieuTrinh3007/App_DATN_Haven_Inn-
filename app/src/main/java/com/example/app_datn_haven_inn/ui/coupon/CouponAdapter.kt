package com.example.app_datn_haven_inn.ui.coupon

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.CouponModel

class CouponAdapter(
    private var couponList: List<CouponModel>,
    private val onCouponUsed: (CouponModel) -> Unit,
    private val trangThai: Int
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        return CouponViewHolder(view)
    }


    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = couponList[position]
        holder.discountCode.text = "Mã: ${coupon.maGiamGia}"
        holder.discountDescription.text = "Giảm ${coupon.giamGia*100}% tối đa ${coupon.giamGiaToiDa}đ cho các đơn hàng từ ${coupon.dieuKienToiThieu}đ"
        holder.expiryDate.text = "Ngày hết hạn: ${coupon.ngayKetThuc}"

        if (trangThai != 1) {
            holder.useButton.visibility = View.GONE
        } else {
            holder.useButton.visibility = View.VISIBLE
            holder.useButton.setOnClickListener {
                onCouponUsed(coupon)
            }
        }
    }

    override fun getItemCount(): Int = couponList.size

    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val discountCode: TextView = itemView.findViewById(R.id.discount_code)
        val discountDescription: TextView = itemView.findViewById(R.id.discount_description)
        val expiryDate: TextView = itemView.findViewById(R.id.expiry_date)
        val useButton: TextView = itemView.findViewById(R.id.use_button)
    }

    fun updateCoupons(newCoupons: List<CouponModel>) {
        couponList = newCoupons
        notifyDataSetChanged()
    }
}
