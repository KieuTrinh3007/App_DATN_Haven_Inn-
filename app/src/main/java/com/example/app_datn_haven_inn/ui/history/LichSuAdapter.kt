package com.example.app_datn_haven_inn.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel1
import com.example.app_datn_haven_inn.databinding.ItemLichsuBinding
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class LichSuAdapter(private val historyList: List<HoaDonModel1>) :
    RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder>() {

    class LichSuViewHolder(val binding: ItemLichsuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LichSuViewHolder {
        val binding = ItemLichsuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LichSuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LichSuViewHolder, position: Int) {
        val hoaDon = historyList[position]
        val binding = holder.binding

        // Kiểm tra chiTiet không phải null và không rỗng
        val chitiet = hoaDon.chiTiet.takeIf { it.isNotEmpty() } ?: ArrayList<ChiTietHoaDonModel1>()

        // Hiển thị thông tin từng chi tiết
        val tenloaiList = chitiet.joinToString { it.tenLoaiPhong ?: "Không có tên phòng" }
        binding.txtTenPhong.text = "Tên loại phòng: $tenloaiList"

        val soPhongList = chitiet.joinToString { it.soPhong ?: "Không có số phòng" }
        binding.txtSoPhong.text = "Phòng: $soPhongList"
        binding.txtNgayNhanPhong.text =  "Ngày nhận phòng: ${formatDateTime(hoaDon.ngayNhanPhong)}"

        // Hiển thị tổng tiền, sử dụng định dạng số
        binding.txtTongTien.text = "Tổng tiền: ${formatMoney(hoaDon.tongTien)} VND"

        // Hiển thị ảnh phòng (chỉ lấy ảnh của phòng đầu tiên trong danh sách chi tiết)
        val firstRoom = chitiet.firstOrNull()
        if (firstRoom != null && firstRoom.hinhAnh.isNotEmpty()) {
            Picasso.get().load(firstRoom.hinhAnh.first()).into(binding.imgPhong)
        }

        // Xử lý nút hành động
        binding.btnAction.setOnClickListener {
            // Xử lý hành động đặt lại, đánh giá, v.v.
            // Ví dụ: chuyển hướng đến một màn hình khác
        }
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

    private fun formatMoney(amount: Double): String {
        val formatter = DecimalFormat("#,###.##")  // Định dạng số với dấu phân cách hàng nghìn
        return formatter.format(amount)
    }

    override fun getItemCount(): Int = historyList.size
}
