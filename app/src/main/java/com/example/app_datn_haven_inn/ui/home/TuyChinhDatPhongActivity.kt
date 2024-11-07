package com.example.app_datn_haven_inn.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityTuyChinhDatPhongBinding
import com.example.app_datn_haven_inn.databinding.DialogLocLoaiPhongBinding
import java.util.Calendar

class TuyChinhDatPhongActivity : BaseActivity<ActivityTuyChinhDatPhongBinding, BaseViewModel>() {

    var isBreakfast = false
    override fun createBinding() = ActivityTuyChinhDatPhongBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()


    override fun initView() {
        super.initView()

        val calendar = Calendar.getInstance()
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val currentDate = "$formattedDay/$formattedMonth/${calendar.get(Calendar.YEAR)}"
        binding.tvNgay.text = currentDate
        binding.tvNgay1.text = currentDate

        binding.tvDat.setOnClickListener{
            showDialogLoaiPhong()
        }

        binding.ivCalendar.setOnClickListener{

            // Lấy ngày hiện tại
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Hiển thị DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                    binding.tvNgay.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }

        binding.ivCalendar1.setOnClickListener{


            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                    binding.tvNgay1.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }

        updateBreakfastIcon()
        binding.rdThemBuaSang.setOnClickListener{
            isBreakfast = true
            updateBreakfastIcon()
        }

        binding.rdKhongBuaSang.setOnClickListener{
            isBreakfast = false
            updateBreakfastIcon()
        }

    }

    private fun updateBreakfastIcon(){
        if (isBreakfast){
            binding.rdThemBuaSang.setBackgroundResource(R.drawable.iv_breakfast)
            binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_no_breakfast)

        }else{
            binding.rdThemBuaSang.setBackgroundResource(R.drawable.iv_no_breakfast)
            binding.rdKhongBuaSang.setBackgroundResource(R.drawable.iv_breakfast)

        }
    }

    private fun showDialogLoaiPhong() {
        val dialog = Dialog(this)
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
        ivClose.setOnClickListener {
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

