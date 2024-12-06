package com.example.app_datn_haven_inn.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.HoaDonModel

class LichSuAdapter(private val hoaDonList: List<HoaDonModel>) :
    RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LichSuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lichsu, parent, false)
        return LichSuViewHolder(view)
    }

    override fun onBindViewHolder(holder: LichSuViewHolder, position: Int) {
        val hoaDon = hoaDonList[position]

        // Gán giá trị cho các View trong item
        holder.txtTenLoaiPhong.text = "Tên phòng: ${hoaDon.phong?.loaiPhong?.tenLoaiPhong ?: "Chưa có thông tin"}"

        holder.txtSoPhong.text = "Số phòng: ${hoaDon.phong?.soPhong ?: "Chưa có thông tin"}"

        holder.txtNgayNhanPhong.text = "Ngày nhận phòng: ${hoaDon.ngayNhanPhong}"
        holder.txtTongTien.text = "Tổng tiền: ${hoaDon.tongTien} đ"
    }

    override fun getItemCount(): Int {
        return hoaDonList.size
    }

    // ViewHolder để liên kết với các View trong item
    inner class LichSuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSoPhong: TextView = itemView.findViewById(R.id.txtSoPhong)
        val txtTenLoaiPhong: TextView = itemView.findViewById(R.id.txtTenPhong)
        val txtNgayNhanPhong: TextView = itemView.findViewById(R.id.txtNgayNhanPhong)
        val txtTongTien: TextView = itemView.findViewById(R.id.txtTongTien)
    }
}
