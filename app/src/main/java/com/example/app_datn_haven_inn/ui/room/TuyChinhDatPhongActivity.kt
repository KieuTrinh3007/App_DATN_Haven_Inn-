package com.example.app_datn_haven_inn.ui.room

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.databinding.ActivityTuyChinhDatPhongBinding
import com.example.app_datn_haven_inn.ui.booking.BookingActivity
import com.example.app_datn_haven_inn.ui.room.adapter.SelectedRoomAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TuyChinhDatPhongAdapter
import com.example.app_datn_haven_inn.viewModel.PhongViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class TuyChinhDatPhongActivity : BaseActivity<ActivityTuyChinhDatPhongBinding, PhongViewModel>() {


    private var adapter: TuyChinhDatPhongAdapter? = null
    private var selectedRoomAdapter: SelectedRoomAdapter? = null
    override fun createBinding() = ActivityTuyChinhDatPhongBinding.inflate(layoutInflater)
    override fun setViewModel() = PhongViewModel()
    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null
    private var totalPrice: Double = 0.0
    private var selectedRooms: List<PhongModel> = emptyList()
    private var guestCountsMap: HashMap<String, Double>? = null
    private var ngayNhanPhong: String = ""
    private var ngayTraPhong: String = ""
    var idLoaiPhong = ""
    var gia = 0
    var tvSLKhach = 1
    var numberOfNights: Int = 0

    override fun onResume() {
        super.onResume()
        viewModel.getListPhongByIdLoaiPhong(idLoaiPhong.toString())
        viewModel.phongListByIdLoaiPhong.observe(this) { list ->
            if (list != null) {
                adapter?.let {
                    it.listSoPhong = list
                    it.notifyDataSetChanged()
                }
            }
        }
    }
    override fun initView() {
        super.initView()
        idLoaiPhong = intent.getStringExtra("id_LoaiPhong").toString()
        gia = intent.getIntExtra("giaTien", 100000)
        tvSLKhach = intent.getIntExtra("soLuongKhach", 1)
        adapter = TuyChinhDatPhongAdapter(emptyList(), onRoomClick = { selectedRoom, isSelected ->
            if (isSelected) {
                selectedRoomAdapter?.addRoom(selectedRoom)
            } else {
                selectedRoomAdapter?.removeRoom(selectedRoom)
            }
            updateTotalPrice()
            updateTvDatButtonState()
        })

        selectedRoomAdapter = SelectedRoomAdapter(mutableListOf(), tvSLKhach, gia.toInt())
        selectedRoomAdapter?.onTotalPriceChanged = {
            updateTotalPrice()

        }


        binding.rvChiTietGiaPhong.adapter = selectedRoomAdapter
        binding.rvChonSoPhong.adapter = adapter

        updateTvDatButtonState()
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

        // Lấy ngày hiện tại và định dạng
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val currentDate = "$formattedDay/$formattedMonth/${calendar.get(Calendar.YEAR)}"

        // Hiển thị ngày hiện tại lên UI
        binding.tvNgay.text = currentDate
        binding.tvNgay1.text = currentDate

        // Gán ngày nhận phòng và trả phòng
        selectedStartDate = currentDate
        selectedEndDate = currentDate

        // Đảm bảo ngày trả phòng luôn lớn hơn ngày nhận phòng 1 ngày
        calendar.add(Calendar.DAY_OF_MONTH, 1)  // Tăng thêm 1 ngày
        val nextDay = calendar.time

        // Định dạng lại ngày trả phòng (ngày tiếp theo của ngày hiện tại)
        val formattedEndDay = String.format("%02d", nextDay.date)
        val formattedEndMonth = String.format("%02d", nextDay.month + 1)
        val nextDate =
            "$formattedEndDay/$formattedEndMonth/${nextDay.year + 1900}"  // Thêm 1900 vì year trả về từ Calendar bắt đầu từ 1900

        binding.tvNgay1.text = nextDate
        selectedEndDate = nextDate

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvDat.setOnClickListener {
            totalPrice = (selectedRoomAdapter?.calculateTotalPrice(1) ?: 0).toDouble()
            selectedRooms = selectedRoomAdapter?.getSelectedRooms() ?: emptyList()
            guestCountsMap = HashMap(
                selectedRoomAdapter?.guestCounts ?: emptyMap()
            )?.filterValues { it > 0 } as HashMap<String, Double>?

            viewModel.saveBookingData(selectedRooms, totalPrice.toInt())


            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("totalPrice", totalPrice)
            intent.putParcelableArrayListExtra("selectedRooms", ArrayList(selectedRooms))
            intent.putExtra("startDate", selectedStartDate)
            intent.putExtra("endDate", selectedEndDate)
            intent.putExtra("guestCountsMap", guestCountsMap)
            val tongTien = binding.tvTong.text.toString()
            intent.putExtra("tongTien", tongTien)

            val hoaDonList = selectedRoomAdapter?.getHoaDonList() ?: emptyList()
            Log.d("SelectedRoomAdapter", "Thông tin hóa đơn: $hoaDonList")
            intent.putParcelableArrayListExtra("chiTiet", ArrayList(hoaDonList))
            startActivity(intent)
        }

        numberOfNights = 1
        binding.tvSoDem.text = "$numberOfNights" + " đêm"

        binding.ivCalendar.setOnClickListener {
            // Lấy ngày hiện tại
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Tạo DatePickerDialog cho ngày nhận phòng
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                    // Cập nhật ngày nhận phòng
                    selectedStartDate = selectedDate
                    binding.tvNgay.text = selectedDate
                    ngayNhanPhong = "$selectedYear-$formattedMonth-$formattedDay"

                    // Reset dữ liệu và tải danh sách phòng mới
                    selectedRoomAdapter?.resetSelectedRooms()
                    adapter?.clearSelectedRoom()
                    updateTotalPrice()

                    binding.tvSoDem.text = numberOfNights.toString() + " đêm"
                    viewModel.getListPhongByDate(
                        idLoaiPhong.toString(),
                        ngayNhanPhong,
                        ngayTraPhong
                    )
                    viewModel.phongListByDate.observe(this) { list ->

                        Log.d("PhongViewModel", "List phong by date: $list")
                        if (list != null) {
                            val filteredList =
                                list.filter { it.trangThai == 0 || it.trangThai == 1 }
                            adapter?.let {
                                it.listSoPhong = filteredList
                                it.notifyDataSetChanged()
                            }
                        }
                    }
                },
                year, month, day

            )

            // Đặt giới hạn ngày
            datePickerDialog.datePicker.minDate = calendar.timeInMillis // Ngày hiện tại

            if (!selectedEndDate.isNullOrEmpty()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val endDate = dateFormat.parse(selectedEndDate)
                if (endDate != null) {
                    datePickerDialog.datePicker.maxDate = endDate.time // Ngày trả phòng
                }
            }

            datePickerDialog.show()
        }

        binding.ivCalendar1.setOnClickListener {
            if (selectedStartDate.isNullOrEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày nhận phòng trước!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Lấy ngày nhận phòng đã chọn (selectedStartDate) và cộng thêm 1 ngày để làm ngày trả phòng
            val startCalendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDate = dateFormat.parse(selectedStartDate)

            if (startDate != null) {
                startCalendar.time = startDate
                startCalendar.add(Calendar.DAY_OF_MONTH, 1)  // Tăng thêm 1 ngày

                val year = startCalendar.get(Calendar.YEAR)
                val month = startCalendar.get(Calendar.MONTH)
                val day = startCalendar.get(Calendar.DAY_OF_MONTH)

                // Hiển thị DatePickerDialog cho ngày trả phòng (ngày sau ngày nhận phòng)
                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, selectedYear, selectedMonth, selectedDay ->

                        val formattedDay = String.format("%02d", selectedDay)
                        val formattedMonth = String.format("%02d", selectedMonth + 1)
                        val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                        selectedEndDate = selectedDate
                        binding.tvNgay1.text = selectedDate
                        selectedRoomAdapter!!.resetSelectedRooms()
                        adapter?.clearSelectedRoom()
                        updateTotalPrice()
                        ngayTraPhong = "$selectedYear-$formattedMonth-$formattedDay"
                        binding.tvSoDem.text = numberOfNights.toString() + " đêm"
                        viewModel.getListPhongByDate(
                            idLoaiPhong.toString(),
                            ngayNhanPhong,
                            ngayTraPhong
                        )
                        viewModel.phongListByDate.observe(this) { list ->
                            Log.d("PhongViewModel", "List phong by date: $list")
                            if (list != null) {
                                val filteredList =
                                    list.filter { it.trangThai == 0 || it.trangThai == 1 }
                                adapter?.let {
                                    it.listSoPhong = filteredList
                                    it.notifyDataSetChanged()
                                }
                            }

                        }
                    },
                    year, month, day
                )
                datePickerDialog.datePicker.minDate = startCalendar.timeInMillis
                datePickerDialog.show()
            }
        }
    }


    private fun updateTvDatButtonState() {
        val isEnabled = selectedRoomAdapter?.getSelectedRooms()?.isNotEmpty() == true
        binding.tvDat.isEnabled = isEnabled
        binding.tvDat.alpha = if (isEnabled) 1.0f else 0.5f
    }

    private fun updateTotalPrice() {
        // Tính số đêm nếu đã chọn ngày
        if (selectedStartDate != null && selectedEndDate != null) {
            numberOfNights = calculateNumberOfNights(selectedStartDate, selectedEndDate) ?: 0
        } else {
            numberOfNights = 1  // Nếu chưa chọn ngày, mặc định là 1 đêm
        }

        // Cập nhật số đêm lên UI
        binding.tvSoDem.text = "$numberOfNights đêm"

        // Cập nhật tổng giá tiền
        if (numberOfNights > 0) {
            val totalPrice = selectedRoomAdapter?.calculateTotalPrice(numberOfNights) ?: 0
            binding.tvTong.text = formatCurrency(totalPrice)
        } else {
            binding.tvTong.text = "0 đ"
        }
        updateTvDatButtonState()

    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount) + " đ"
    }

    private fun calculateNumberOfNights(startDate: String?, endDate: String?): Int? {
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) return 0

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)
            if (start != null && end != null && !end.before(start)) {
                val diffInMillis = end.time - start.time
                TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

