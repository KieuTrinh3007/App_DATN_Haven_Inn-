package com.example.app_datn_haven_inn.ui.room

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityTuyChinhDatPhongBinding
import com.example.app_datn_haven_inn.ui.booking.BookingFragment
//import com.example.app_datn_haven_inn.ui.booking.BookingFragment
import com.example.app_datn_haven_inn.ui.room.adapter.SelectedRoomAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TuyChinhDatPhongAdapter
import com.example.app_datn_haven_inn.viewModel.PhongViewModel
import java.util.Calendar

class TuyChinhDatPhongActivity : BaseActivity<ActivityTuyChinhDatPhongBinding, PhongViewModel>() {


    private var adapter: TuyChinhDatPhongAdapter? = null
    private var selectedRoomAdapter: SelectedRoomAdapter? = null
    override fun createBinding() = ActivityTuyChinhDatPhongBinding.inflate(layoutInflater)
    override fun setViewModel() = PhongViewModel()


    override fun initView() {
        super.initView()
        val idLoaiPhong = intent.getStringExtra("id_LoaiPhong")
        val tvGiaPhong = intent.getStringExtra("giaTien").toString()
        adapter = TuyChinhDatPhongAdapter(emptyList(), onRoomClick = { selectedRoom, isSelected ->
            if (isSelected) {
                selectedRoomAdapter?.addRoom(selectedRoom, tvGiaPhong)
            } else {
                selectedRoomAdapter?.removeRoom(selectedRoom)
            }
        })
        // Adapter cho rv_ChiTietGiaPhong
        val selectedRoomList = mutableListOf<Pair<String, String>>()
        selectedRoomAdapter = SelectedRoomAdapter(selectedRoomList)
        binding.rvChiTietGiaPhong.adapter = selectedRoomAdapter
        binding.rvChonSoPhong.adapter = adapter


        viewModel.getListPhongByIdLoaiPhong(idLoaiPhong.toString())
        viewModel.phongListByIdLoaiPhong.observe(this) { list ->
            if (list != null) {
                adapter?.let {
                    it.listSoPhong = list
                    it.notifyDataSetChanged()
                }
            }
        }

        val calendar = Calendar.getInstance()
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val currentDate = "$formattedDay/$formattedMonth/${calendar.get(Calendar.YEAR)}"
        binding.tvNgay.text = currentDate
        binding.tvNgay1.text = currentDate

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvDat.setOnClickListener {
            binding.flBooking.visibility = View.VISIBLE
            binding.clAcivity.visibility =  View.GONE

            val intent = Intent(this, BookingFragment::class.java)
            startActivity(intent)
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




    }




}

