package com.example.app_datn_haven_inn.ui.room.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentPhongNghiBinding
import com.example.app_datn_haven_inn.ui.room.PhongNghiAdapter
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import java.util.Calendar


class PhongNghiFragment : BaseFragment<FragmentPhongNghiBinding>() {

    private var adapter: PhongNghiAdapter? = null
    private lateinit var loaiPhongViewModel: LoaiPhongViewModel
    override fun inflateViewBinding(): FragmentPhongNghiBinding {
        return FragmentPhongNghiBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()
        viewBinding.rcvDanhSachPhong.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhongNghiAdapter(emptyList())
        viewBinding.rcvDanhSachPhong.adapter = adapter
        loaiPhongViewModel = ViewModelProvider(requireActivity())[LoaiPhongViewModel::class.java]
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

        val calendar = Calendar.getInstance()
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val currentDate = "$formattedDay/$formattedMonth/${calendar.get(Calendar.YEAR)}"

        viewBinding.tvTuNgay.text = currentDate
        viewBinding.tvDenNgay.text = currentDate

        viewBinding.tvTuNgay.setOnClickListener{

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

        viewBinding.tvDenNgay.setOnClickListener{


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

        viewBinding.tvTuKhoang.setOnClickListener{
            showDialogLoaiPhong()
        }

        viewBinding.tvDenKhoang.setOnClickListener{
            showDialogLoaiPhong()
        }

        viewBinding.llSoNguoi.setOnClickListener{
            showDialogLoaiPhong()
        }
    }

        private fun showDialogLoaiPhong() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loc_loai_phong)
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
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
        val edGiaToiThieu = dialog.findViewById<TextView>(R.id.etGiaToiThieu)
        val edGiaToiDa = dialog.findViewById<TextView>(R.id.etGiaToiDa)
            val tvXong = dialog.findViewById<TextView>(R.id.tvXong)
        ivClose.setOnClickListener {
            dialog.dismiss()
        }

            tvXong.setOnClickListener {
                dialog.dismiss()
            }
        var soLuongNguoiLon = tvSoLuongNguoiLon.text.toString().toInt()
        var soLuongTreEm = tvSoLuongTreEm.text.toString().toInt()

        ivPlus.setOnClickListener {
            soLuongNguoiLon++
            tvSoLuongNguoiLon.text = soLuongNguoiLon.toString()
        }
        ivMinus.setOnClickListener {
            if (soLuongNguoiLon > 0) {
                soLuongNguoiLon--
                tvSoLuongNguoiLon.text = soLuongNguoiLon.toString()
            }
        }

        ivPlus1.setOnClickListener {
            soLuongTreEm++
            tvSoLuongTreEm.text = soLuongTreEm.toString()
        }

        ivMinus1.setOnClickListener {
            if (soLuongTreEm > 0) {
                soLuongTreEm--
                tvSoLuongTreEm.text = soLuongTreEm.toString()
            }
        }
    }
}