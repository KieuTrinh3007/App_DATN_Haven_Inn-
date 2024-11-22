package com.example.app_datn_haven_inn.ui.room.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import com.example.app_datn_haven_inn.database.repository.YeuThichRepository
import com.example.app_datn_haven_inn.databinding.FragmentPhongNghiBinding
import com.example.app_datn_haven_inn.ui.room.PhongNghiAdapter
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale


class PhongNghiFragment : BaseFragment<FragmentPhongNghiBinding>() {

    private var adapter: PhongNghiAdapter? = null
    private lateinit var loaiPhongViewModel: LoaiPhongViewModel
    private lateinit var yeuThichViewModel: YeuThichViewModel

    override fun inflateViewBinding(): FragmentPhongNghiBinding {
        return FragmentPhongNghiBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()

        viewBinding.rcvDanhSachPhong.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhongNghiAdapter(emptyList())
        viewBinding.rcvDanhSachPhong.adapter = adapter

        loaiPhongViewModel = ViewModelProvider(requireActivity())[LoaiPhongViewModel::class.java]
        yeuThichViewModel = ViewModelProvider(requireActivity())[YeuThichViewModel::class.java]

        loaiPhongViewModel.getListloaiPhong()

        loaiPhongViewModel.loaiPhongList.observe(this) { list ->
            Log.d("PhongNghiFragment", "List size: ${list?.size}")
            if (list != null) {

                adapter?.let {
                    it.listPhong = list
                    it.notifyDataSetChanged()
                }
            }
        }

        adapter?.setonFavotiteSelected { phong ->
            // Lấy idNguoiDung từ SharedPreferences
            val sharedPreferences =
                requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val idNguoiDung = sharedPreferences?.getString("idNguoiDung", null)
            Log.d("PhongNghiFragment", "idNguoiDung: $idNguoiDung")
            if (idNguoiDung == null) {
                Toast.makeText(
                    requireActivity(),
                    "Không tìm thấy thông tin người dùng",
                    Toast.LENGTH_SHORT
                ).show()
                return@setonFavotiteSelected
            }

            if (phong.isFavorite) {
                val yeuThich = YeuThichModel(
                    id = "",
                    id_LoaiPhong = phong.id,
                    id_NguoiDung = "6724a13a2378017ace035c51",

                    )
                yeuThichViewModel.addyeuThich(yeuThich)

                yeuThichViewModel.isyeuThichAdded.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Thêm yêu thích thất bại", Toast.LENGTH_SHORT)
                            .show()
                        phong.isFavorite = false
                        adapter?.notifyItemChanged(adapter?.listPhong?.indexOf(phong) ?: 0)
                    }
                }
            } else {

                yeuThichViewModel.deleteyeuThich(phong.id)


                yeuThichViewModel.isyeuThichDeleted.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Xóa yêu thích thất bại", Toast.LENGTH_SHORT).show()
                        phong.isFavorite = true
                        adapter?.notifyItemChanged(adapter?.listPhong?.indexOf(phong) ?: 0)
                    }
                }
            }
        }


        val calendar = Calendar.getInstance()
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val currentDate = "$formattedDay/$formattedMonth/${calendar.get(Calendar.YEAR)}"

        viewBinding.tvTuNgay.text = currentDate
        viewBinding.tvDenNgay.text = currentDate

        viewBinding.tvTuNgay.setOnClickListener {

            // Lấy ngày hiện tại
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Hiển thị DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->

                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                    viewBinding.tvTuNgay.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }

        viewBinding.tvDenNgay.setOnClickListener {


            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->

                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                    viewBinding.tvDenNgay.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }

        viewBinding.tvTuKhoang.setOnClickListener {
            showDialogLoaiPhong("TuKhoang")
        }

        viewBinding.tvDenKhoang.setOnClickListener {
            showDialogLoaiPhong("DenKhoang")
        }

        viewBinding.llSoNguoi.setOnClickListener {
            showDialogLoaiPhong("SoNguoi")
        }
    }

    private fun showDialogLoaiPhong(dataType: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loc_loai_phong)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val ivClose = dialog.findViewById<ImageView>(R.id.iv_close)
        val ivMinus = dialog.findViewById<ImageView>(R.id.iv_minus)
        val ivPlus = dialog.findViewById<ImageView>(R.id.iv_plus)
        val tvSoLuongNguoiLon = dialog.findViewById<TextView>(R.id.tvSoLuongNguoiLon)
        val ivMinus1 = dialog.findViewById<ImageView>(R.id.iv_minus1)
        val ivPlus1 = dialog.findViewById<ImageView>(R.id.iv_plus1)
        val tvSoLuongTreEm = dialog.findViewById<TextView>(R.id.tvSoLuongTreEm)
        val edGiaToiThieu = dialog.findViewById<EditText>(R.id.etGiaToiThieu)
        val edGiaToiDa = dialog.findViewById<EditText>(R.id.etGiaToiDa)
        val tvXong = dialog.findViewById<TextView>(R.id.tvXong)

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        // Lấy giá trị hiện tại từ layout chính
        val soLuongNguoiLon = viewBinding.tvSoKhach.text.toString().split(" ")[0].toIntOrNull() ?: 0
        val soLuongTreEm = 0 // Bạn có thể lấy số lượng trẻ em từ TextView tương ứng nếu có.
        val giaToiThieu = viewBinding.tvTuKhoang.text.toString().replace(" VNĐ", "0")
        val giaToiDa = viewBinding.tvDenKhoang.text.toString().replace(" VNĐ", "0")

        // Đặt các giá trị lấy được vào trong dialog
        tvSoLuongNguoiLon.text = soLuongNguoiLon.toString()
        tvSoLuongTreEm.text = soLuongTreEm.toString()
        edGiaToiThieu.setText(giaToiThieu)
        edGiaToiDa.setText(giaToiDa)

        var currentSoLuongNguoiLon = soLuongNguoiLon
        var currentSoLuongTreEm = soLuongTreEm

        // Hàm cập nhật trạng thái của nút giảm (ivMinus)
        fun updateMinusButtonState(button: ImageView, value: Int, minValue: Int) {
            if (value <= minValue) {
                button.isEnabled = false
                button.alpha = 0.3f
            } else {
                button.isEnabled = true
                button.alpha = 1.0f
            }
        }

        // Cập nhật trạng thái nút ivMinus ban đầu
        updateMinusButtonState(ivMinus, currentSoLuongNguoiLon, 1)

        ivPlus.setOnClickListener {
            currentSoLuongNguoiLon++
            tvSoLuongNguoiLon.text = currentSoLuongNguoiLon.toString()
            updateMinusButtonState(ivMinus, currentSoLuongNguoiLon, 1)
        }

        ivMinus.setOnClickListener {
            if (currentSoLuongNguoiLon > 1) {
                currentSoLuongNguoiLon--
                tvSoLuongNguoiLon.text = currentSoLuongNguoiLon.toString()
                updateMinusButtonState(ivMinus, currentSoLuongNguoiLon, 1)
            }
        }

        // Cập nhật trạng thái nút ivMinus1 ban đầu
        updateMinusButtonState(ivMinus1, currentSoLuongTreEm, 0)

        ivPlus1.setOnClickListener {
            currentSoLuongTreEm++
            tvSoLuongTreEm.text = currentSoLuongTreEm.toString()
            updateMinusButtonState(ivMinus1, currentSoLuongTreEm, 0)
        }

        ivMinus1.setOnClickListener {
            if (currentSoLuongTreEm > 0) {
                currentSoLuongTreEm--
                tvSoLuongTreEm.text = currentSoLuongTreEm.toString()
                updateMinusButtonState(ivMinus1, currentSoLuongTreEm, 0)
            }
        }


        tvXong.setOnClickListener {
            val giaToiThieu = edGiaToiThieu.text.toString().trim()
            val giaToiDa = edGiaToiDa.text.toString().trim()

            // Tính tổng số khách
            val soNguoiLon = tvSoLuongNguoiLon.text.toString().toInt()
            val soTreEm = tvSoLuongTreEm.text.toString().toInt()
            val tongSoNguoi = soNguoiLon + soTreEm

            // Cập nhật vào các TextView tương ứng
            viewBinding.tvSoKhach.text = "$tongSoNguoi khách"
            viewBinding.tvTuKhoang.text = "$giaToiThieu"
            viewBinding.tvDenKhoang.text = "$giaToiDa"


            dialog.dismiss()
        }
    }


}