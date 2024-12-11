package com.example.app_datn_haven_inn.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.databinding.ItemThongbaoBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ThongBaoAdapter(
    var thongBaoList: MutableList<ThongBaoModel>,
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder>() {

    class ThongBaoViewHolder(val binding: ItemThongbaoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThongBaoViewHolder {
        val binding = ItemThongbaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThongBaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThongBaoViewHolder, position: Int) {
        val thongBao = thongBaoList[position]

        with(holder.binding) {
            tvTieuDe.text = thongBao.tieuDe
            tvNoiDung.text = thongBao.noiDung

            // Định dạng ngày giờ và gán vào TextView
            tvNgayGui.text = formatDateTime(thongBao.ngayGui)

            // Đổi màu nền dựa trên trạng thái của thông báo
            root.setBackgroundResource(
                if (!thongBao.trangThai) R.drawable.item_border_active
                else R.drawable.item_border_inactive
            )

            root.setOnClickListener {
                thongBao.trangThai = true // Đánh dấu thông báo là đã đọc
                notifyItemChanged(position) // Cập nhật chỉ mục trong RecyclerView
                onItemClick(position)
            }
        }
    }


    override fun getItemCount(): Int = thongBaoList.size

    /**
     * Cập nhật danh sách thông báo mới
     */
    fun updateList(newList: List<ThongBaoModel>) {
        thongBaoList.clear()
        thongBaoList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<ThongBaoModel> {
        return thongBaoList
    }
    private fun formatDateTime(dateString: String): String {
        return try {
            // Định dạng dữ liệu đầu vào từ server
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Dữ liệu từ server là UTC

            // Parse dữ liệu đầu vào thành đối tượng Date
            val date = inputFormat.parse(dateString)

            // Định dạng dữ liệu đầu ra (giờ phút và ngày tháng năm)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // Định dạng giờ phút
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Định dạng ngày tháng năm

            // Ghép kết quả đầu ra
            "${timeFormat.format(date)} ${dateFormat.format(date)}"
        } catch (e: Exception) {
            e.printStackTrace()
            "N/A"
        }
    }



}
