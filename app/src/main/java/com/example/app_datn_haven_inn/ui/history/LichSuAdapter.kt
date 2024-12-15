package com.example.app_datn_haven_inn.ui.history

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel1
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.databinding.ItemLichsuBinding
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class LichSuAdapter(
    private var historyList: List<HoaDonModel1>,
    private val onActionClick: (HoaDonModel1) -> Unit,
    private val context: Context,
    private val onDanhGiaClick: (String, String, String) -> Unit
) : RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder>() {
class LichSuAdapter(private var historyList: List<HoaDonModel1>, private val onActionClick: (HoaDonModel1) -> Unit, private val onCancelClick: (HoaDonModel1) -> Unit) :
    RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder>() {

    class LichSuViewHolder(val binding: ItemLichsuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LichSuViewHolder {
        val binding = ItemLichsuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LichSuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LichSuViewHolder, position: Int) {
        val hoaDon = historyList[position]
        val binding = holder.binding

        val chitiet = hoaDon.chiTiet.takeIf { it.isNotEmpty() } ?: ArrayList<ChiTietHoaDonModel1>()
        val tenloaiList = chitiet.joinToString { it.tenLoaiPhong ?: "Không có tên phòng" }
        binding.txtTenPhong.text = "Tên loại phòng: $tenloaiList"

        val soPhongList = chitiet.joinToString { it.soPhong ?: "Không có số phòng" }
        binding.txtSoPhong.text = "Phòng: $soPhongList"
        binding.txtNgayNhanPhong.text = "Ngày nhận phòng: ${formatDateTime(hoaDon.ngayNhanPhong)}"
        binding.txtTongTien.text = "Tổng tiền: ${formatMoney(hoaDon.tongTien)} VND"

        // Thiết lập hình ảnh
        val firstRoom = chitiet.firstOrNull()
        if (firstRoom != null && !firstRoom.hinhAnh.isNullOrEmpty()) {
            Picasso.get()
                .load(firstRoom.hinhAnh.first())
                .resize(1024, 1024)   // Resize to a smaller size (can adjust the size as needed)
                .centerInside()        // Make sure the image fits the ImageView
                .into(binding.imgPhong)
        } else {
            binding.imgPhong.setImageResource(R.drawable.img_13) // Thay thế bằng ảnh mặc định
        }



        // Hiển thị/ẩn các nút theo trạng thái
        when (hoaDon.trangThai) {
            0 -> { // Đã nhận phòng
                binding.btnAction.visibility = View.GONE
                binding.btnDanhGia.visibility = View.GONE
                binding.btnHuy.visibility = View.GONE
            0 -> { // Da nhan phong
                if (hoaDon.checkDanhGia) {
                    binding.btnDanhGia.visibility = View.GONE
                    binding.btnHuy.visibility = View.GONE
                    binding.btnAction.visibility = View.VISIBLE
                } else {
                    binding.btnHuy.visibility = View.GONE
                    binding.btnAction.visibility = View.VISIBLE
                    binding.btnDanhGia.visibility = View.VISIBLE
                }
            }
            1 -> { // Đã thanh toán
                binding.btnAction.visibility = View.GONE
                binding.btnDanhGia.visibility = View.GONE
                binding.btnHuy.visibility = View.VISIBLE
            }
            2 -> { // Đã hủy
                binding.btnAction.visibility = View.VISIBLE
                binding.btnDanhGia.visibility = View.GONE
                binding.btnHuy.visibility = View.GONE
            }
            3 -> { // Đã trả phòng
                binding.btnAction.visibility = View.VISIBLE
                binding.btnDanhGia.visibility = View.VISIBLE
                binding.btnHuy.visibility = View.GONE
            }

            else -> {
                binding.btnAction.visibility = View.GONE
                binding.btnDanhGia.visibility = View.GONE
                binding.btnHuy.visibility = View.GONE
            }
        }

        // Sự kiện khi nhấn nút Hủy
        binding.btnHuy.setOnClickListener {
            onCancelClick(hoaDon) // Gọi callback để hủy hóa đơn
        }

        // Các sự kiện khác...
        binding.btnAction.setOnClickListener {
            onActionClick(hoaDon) // Gọi callback khi nhấn nút Action
        }

        Log.d("LichSuAdapter", "id_LoaiPhong All: " + hoaDon.id_LoaiPhong)

        binding.btnDanhGia.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)

            val currentTime = "$day/$month/$year $hour:$minute:$second"
            println("Thời gian hiện tại: $currentTime")

            Log.d("LichSuAdapter", "id_LoaiPhong: " + hoaDon.id_LoaiPhong)

            onDanhGiaClick(hoaDon.id_NguoiDung, hoaDon.id_LoaiPhong, currentTime)

        }
    }

    fun updateData(newHistoryList: List<HoaDonModel1>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }
    private fun formatDateTime(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            "${timeFormat.format(date)} ${dateFormat.format(date)}"
        } catch (e: Exception) {
            e.printStackTrace()
            "N/A"
        }
    }

    private fun formatMoney(amount: Double): String {
        val formatter = DecimalFormat("#,###.##")
        return formatter.format(amount)
    }
    override fun getItemCount(): Int = historyList.size
}
