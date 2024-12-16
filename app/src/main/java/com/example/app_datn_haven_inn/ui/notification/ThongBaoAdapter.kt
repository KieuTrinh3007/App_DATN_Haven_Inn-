package com.example.app_datn_haven_inn.ui.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ThongBaoModel
import com.example.app_datn_haven_inn.databinding.ItemThongbaoBinding
import com.example.app_datn_haven_inn.viewModel.ThongBaoViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ThongBaoAdapter(
    var thongBaoList: MutableList<ThongBaoModel>,
    private val onItemClick: (Int) -> Unit,
    private val thongBaoViewModel: ThongBaoViewModel
) : RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThongBaoViewHolder {
        val binding = ItemThongbaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThongBaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThongBaoViewHolder, position: Int) {
        val thongBao = thongBaoList[position]

        // Kiểm tra trạng thái của thông báo (trangThai)
        if (thongBao.trangThai) {
            // Nếu trạng thái là true (thông báo chưa được xử lý/đọc)
            holder.binding.tvNoiDung.setTextColor(Color.BLACK) // Thông báo chưa đọc thì hiển thị màu đen
            holder.binding.root.setBackgroundResource(R.drawable.item_border_active) // Áp dụng background cho item chưa đọc
        } else {
            // Nếu trạng thái là false (thông báo đã được xử lý/đọc)
            holder.binding.tvNoiDung.setTextColor(Color.GRAY)  // Thông báo đã đọc thì hiển thị màu xám
            holder.binding.root.setBackgroundResource(R.drawable.item_border_inactive) // Áp dụng background cho item đã đọc
        }

        holder.binding.tvNoiDung.text = thongBao.noiDung
        holder.binding.tvTieuDe.text = thongBao.tieuDe
        holder.binding.tvNgayGui.text = formatDateTime(thongBao.ngayGui)

        // Sự kiện click vào item
        holder.itemView.setOnClickListener {
            if (thongBao.trangThai) {
                // Nếu trạng thái là true, thì mới thay đổi thành false
                thongBaoList[position].trangThai = false // Đổi trạng thái thành false
                thongBaoViewModel.updatethongBao(thongBaoList[position].id) // Cập nhật lên server
                notifyItemChanged(position) // Cập nhật giao diện
                onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = thongBaoList.size

    fun updateList(newList: MutableList<ThongBaoModel>) {
        thongBaoList = newList
        notifyDataSetChanged()
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

    class ThongBaoViewHolder(val binding: ItemThongbaoBinding) : RecyclerView.ViewHolder(binding.root)
}
