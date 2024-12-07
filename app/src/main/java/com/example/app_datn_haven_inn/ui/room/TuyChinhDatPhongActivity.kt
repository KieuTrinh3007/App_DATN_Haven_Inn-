package com.example.app_datn_haven_inn.ui.room

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ActivityTuyChinhDatPhongBinding
import com.example.app_datn_haven_inn.ui.booking.BookingActivity
import com.example.app_datn_haven_inn.ui.room.adapter.SelectedRoomAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TuyChinhDatPhongAdapter
import com.example.app_datn_haven_inn.viewModel.PhongViewModel
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class TuyChinhDatPhongActivity : BaseActivity<ActivityTuyChinhDatPhongBinding, PhongViewModel>() {


    private var adapter: TuyChinhDatPhongAdapter? = null
    private var selectedRoomAdapter: SelectedRoomAdapter? = null
    private var phongViewModel: PhongViewModel? = null
    override fun createBinding() = ActivityTuyChinhDatPhongBinding.inflate(layoutInflater)
    override fun setViewModel() = PhongViewModel()
    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null
    private var totalPrice: Double = 0.0
    private var selectedRooms: List<PhongModel> = emptyList()
    private var guestCountsMap: HashMap<String, Double>? = null


    override fun initView() {
        super.initView()
        val idLoaiPhong = intent.getStringExtra("id_LoaiPhong")
        val gia = intent.getIntExtra("giaTien", 100000)
        val tvSLKhach = intent.getIntExtra("soLuongKhach", 1)

        adapter = TuyChinhDatPhongAdapter(emptyList(), onRoomClick = { selectedRoom, isSelected ->
            if (isSelected) {
                selectedRoomAdapter?.addRoom(selectedRoom)
            } else {
                selectedRoomAdapter?.removeRoom(selectedRoom)
            }
        })

        selectedRoomAdapter = SelectedRoomAdapter(mutableListOf(), tvSLKhach, gia.toInt())
        selectedRoomAdapter?.onTotalPriceChanged = {
            updateTotalPrice()
        }


        binding.rvChiTietGiaPhong.adapter = selectedRoomAdapter
        binding.rvChonSoPhong.adapter = adapter


        // list chon so phong -> tuy chinh dat phong
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
        selectedStartDate = currentDate
        selectedEndDate = currentDate

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvDat.setOnClickListener {
            totalPrice = (selectedRoomAdapter?.calculateTotalPrice() ?: 0).toDouble()
            selectedRooms = selectedRoomAdapter?.getSelectedRooms() ?: emptyList()
            guestCountsMap = HashMap(
                selectedRoomAdapter?.guestCounts ?: emptyMap()
            )?.filterValues { it > 0 } as HashMap<String, Double>?

            viewModel.saveBookingData(selectedRooms, totalPrice.toInt())
            phongViewModel?.saveBookingData(selectedRooms, totalPrice.toInt())
            binding.flBooking.visibility = View.VISIBLE
            binding.clAcivity.visibility = View.GONE

            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("totalPrice", totalPrice)
            intent.putParcelableArrayListExtra("selectedRooms", ArrayList(selectedRooms))
            intent.putExtra("startDate", selectedStartDate)
            intent.putExtra("endDate", selectedEndDate)
            intent.putExtra("guestCountsMap", guestCountsMap)
            startActivity(intent)


        }

        binding.ivCalendar.setOnClickListener {

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
                    selectedStartDate = selectedDate
                    binding.tvNgay.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }

        binding.ivCalendar1.setOnClickListener {


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
                    selectedEndDate = selectedDate
                    binding.tvNgay1.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()

        }
    }
    private fun updateTotalPrice() {
        val totalPrice = selectedRoomAdapter?.calculateTotalPrice() ?: 0
        binding.tvTong.text = formatCurrency(totalPrice)
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount) + " đ"
    }


}

