package com.example.app_datn_haven_inn.ui.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.app_datn_haven_inn.Api.CreateOrder
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel1
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.service.CouponService
import com.example.app_datn_haven_inn.database.service.HoaDonService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.database.service.PhongService
import com.example.app_datn_haven_inn.ui.coupon.CouponActivity
import com.example.app_datn_haven_inn.ui.room.RoomDetailActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingActivity : AppCompatActivity() {
    private lateinit var txtGiaDaGiam: TextView
    private lateinit var rdo_zalo: RadioButton
    private lateinit var rdoMomo: RadioButton
    private lateinit var ttZaloPay: LinearLayout
    private lateinit var ttQuaMoMo: LinearLayout
    private lateinit var edtCoupon: EditText
    private lateinit var btnBooking: TextView
    private lateinit var icBack: ImageView
    private var isThanhToan = false
    private lateinit var ngayNhan: TextView
    private lateinit var ngayTra: TextView
    private lateinit var soDem : TextView
    private lateinit var tenKH : TextView
    private lateinit var sdtKH : TextView
    private lateinit var tong: TextView
    private lateinit var soPhongDem : TextView
    var guestCountsMap: HashMap<String, Int>? = null
    var numberOfNights: Int = 0
     private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService()
    }
    private lateinit var hoaDonService: HoaDonService
    private lateinit var phongService: PhongService
    private lateinit var couponService: CouponService

    var idNguoiDung: String? = ""

    var id_NguoiDung: String = ""
    var id_Coupon: String = ""
    var ngayNhanPhong: String = ""
    var ngayTraPhong: String = ""
    var tongKhach: Int = 0
    var tongPhong: Int = 0
    var ngayThanhToan: String = ""
    var phuongThucThanhToan: String = ""
    var trangThai: Int = 1
    var chiTiet: ArrayList<ChiTietHoaDonModel> = ArrayList()
    var tongTien: Double = 0.0

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_booking)

        // truyen du lieu
        val selectedRooms = intent.getParcelableArrayListExtra<PhongModel>("selectedRooms")
        val gia = intent.getDoubleExtra("totalPrice", 0.0)

        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val tongTT = intent.getStringExtra("tongTien")
        val llPhongContainer = findViewById<LinearLayout>(R.id.llPhongContainer)


        // anh xa
        txtGiaDaGiam = findViewById(R.id.txt_giaDaGiam)
        rdo_zalo = findViewById(R.id.rdo_zalo)
        rdoMomo = findViewById(R.id.rdo_momo)
        ttZaloPay = findViewById(R.id.ttZaloPay)
        ttQuaMoMo = findViewById(R.id.ttQuaMoMo)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnBooking = findViewById<TextView>(R.id.btnBooking)
        icBack = findViewById(R.id.ic_back)
        ngayNhan = findViewById(R.id.txt_ngayNhanPhongTT)
        ngayTra = findViewById(R.id.txt_ngayTraPhongTT)
        soDem = findViewById(R.id.txt_soDem)
        tenKH = findViewById(R.id.txt_tenKhachHangTT)
        sdtKH = findViewById(R.id.txt_SDT_khachHangTT)
        tong = findViewById(R.id.txt_tongGia)
        soPhongDem = findViewById(R.id.txt_soPhong_soDem)

        // gan du lieu
        ngayNhan.text = startDate
        ngayTra.text = endDate

        idNguoiDung = intent.getStringExtra("idNguoiDung")
        tong.text = gia.toString()
        tong.text = formatCurrency(gia.toInt())
        Log.d("TAG123", tongTT.toString())
        tongTT?.let {
            tong.text = it
        }


        val idNguoiDung = intent.getStringExtra("idNguoiDung")
            ?: getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("idNguoiDung", null)

        idNguoiDung?.let { fetchUserProfile(it) } ?: run {
            Log.e("BookingActivity", "Không tìm thấy ID người dùng")
            tenKH.text = "Không tìm thấy tên"
            sdtKH.text = "Không tìm thấy số điện thoại"
        }

        if (selectedRooms != null) {
            guestCountsMap =
                intent.getSerializableExtra("guestCountsMap") as? HashMap<String, Int>
            for (phong in selectedRooms) {
                val guestCount = guestCountsMap?.get(phong.soPhong) ?: 1
                val breakfastInfo = if (phong.isBreakfast) "Có bữa sáng" else "Không bữa sáng"
                val roomInfo = if (phong.vip) {
                    // Nếu là phòng VIP
                    "Phòng: ${phong.soPhong} - $guestCount người - Miễn phí bữa sáng"
                } else {
                    // Nếu không phải phòng VIP
                    "Phòng: ${phong.soPhong} - $guestCount người - $breakfastInfo"
                }
                val textView = TextView(this).apply {
                    text = roomInfo
                    textSize = 14f
                    setTextColor(ContextCompat.getColor(this@BookingActivity, R.color.black))
                    setPadding(0, 8, 0, 8)
                    typeface = ResourcesCompat.getFont(this@BookingActivity, R.font.roboto_regular)
                    val paddingTop = resources.getDimensionPixelSize(R.dimen.padding_top)
                    setPadding(0, paddingTop, 0, 8)
                }

                llPhongContainer.addView(textView)
            }
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val checkInDate = dateFormat.parse(startDate) // Ngày nhận phòng
            val checkOutDate = dateFormat.parse(endDate) // Ngày trả phòng

            if (checkInDate != null && checkOutDate != null) {
                val diffInMillis = checkOutDate.time - checkInDate.time
                numberOfNights = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
                soDem.text = "$numberOfNights đêm lưu trú"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BookingFragment", "Lỗi khi tính số đêm: ${e.message}")
        }

        // tong phong va tong de
        val soPhongDem = "${selectedRooms?.size ?: 0} phòng, $numberOfNights đêm"
        soPhongDem.also { this.soPhongDem.text = it }

        hoaDonService = CreateService.createService()

        icBack.setOnClickListener {
            val intent = Intent(this@BookingActivity, RoomDetailActivity::class.java)
            startActivity(intent)
        }
        // Gạch ngang cho TextView
        ttZaloPay.setOnClickListener {
            if (!rdo_zalo.isChecked) {
                rdo_zalo.isChecked = true
                rdoMomo.isChecked = false
                isThanhToan = true
                phuongThucThanhToan = "ZaloPay" // Gán ZaloPay vào phuongThucThanhToan
            }
        }
        ttQuaMoMo.setOnClickListener {
            if (!rdoMomo.isChecked) {
                rdoMomo.isChecked = true
                rdo_zalo.isChecked = false
                isThanhToan = false
                phuongThucThanhToan = "Momo" // Gán Momo vào phuongThucThanhToan
            }
        }

        phongService = CreateService.createService<PhongService>()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val totalString = String.format("%.0f", 10000.0)

//        btnBooking.setOnClickListener {
//            val orderApi = CreateOrder()
//            try {
//                val data = orderApi.createOrder(totalString)
//                val code = data.getString("return_code")
//                if (code == "1") {
//                    val token = data.getString("zp_trans_token")
//                    ZaloPaySDK.getInstance().payOrder(
//                        this@BookingActivity,
//                        token,
//                        "demozpdk://app",
//                        object : PayOrderListener {
//                            override fun onPaymentSucceeded(
//                                p1: String?,
//                                p2: String?,
//                                p3: String?
//                            ) {
//                                // Hiển thị Dialog thông báo thanh toán thành công
//                                showPaymentDialog(
//                                    "Thanh toán thành công",
//                                    "Bạn đã thanh toán thành công!",
//                                    R.drawable.img_16
//                                )
//                            }
//
//                            override fun onPaymentCanceled(p1: String?, p2: String?) {
//                                // Hiển thị Dialog thông báo thanh toán bị hủy
//                                showPaymentDialog(
//                                    "Thanh toán bị hủy",
//                                    "Bạn đã hủy thanh toán!",
//                                    R.drawable.img_18
//                                )
//                            }
//
//                            override fun onPaymentError(
//                                error: ZaloPayError?,
//                                p1: String?,
//                                p2: String?
//                            ) {
//                                // Hiển thị Dialog thông báo lỗi thanh toán
//                                showPaymentDialog(
//                                    "Lỗi thanh toán",
//                                    "Đã xảy ra lỗi khi thanh toán. Vui lòng thử lại!",
//                                    R.drawable.img_18
//                                )
//                            }
//                        })
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

        btnBooking.setOnClickListener {
            addHoaDon()
        }

        edtCoupon.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val intent1 = Intent(this, CouponActivity::class.java)
                startActivityForResult(intent1, 100)
                return@setOnTouchListener true
            }
            false
        }

        tongTien = 1000000.0

        couponService = CreateService.createService<CouponService>()



//        addHoaDon(
////            id_NguoiDung, id_Coupon, ngayNhanPhong, ngayTraPhong, tongKhach,
////            tongPhong, ngayThanhToan, phuongThucThanhToan, trangThai, tongTien, chiTiet
//        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val couponCode = data?.getStringExtra("couponCode") // Lấy mã giảm giá
            val couponId = data?.getStringExtra("idCoupon") // Lấy idCoupon

            if (couponCode != null) {
                edtCoupon.setText(couponCode) // Set mã giảm giá lên EditText
            }

            if (couponId != null) {
                id_Coupon = couponId // Lưu idCoupon để sử dụng sau này
            }
        }
    }

    // Tạo phương thức để hiển thị Dialog với hình ảnh và trạng thái
    private fun showPaymentDialog(title: String, message: String, imageResId: Int) {
        // Tạo layout cho dialog với ImageView và TextView
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 50, 50, 50)
        layout.gravity = Gravity.CENTER // Căn giữa nội dung trong layout

        // Tạo ImageView để hiển thị hình ảnh
        val imageView = ImageView(this)
        val textView = TextView(this)

        // Thiết lập hình ảnh từ drawable bằng imageResId
        imageView.setImageResource(imageResId)

        // Set kích thước cho ImageView
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, // Chiều rộng tự động theo nội dung
            LinearLayout.LayoutParams.WRAP_CONTENT  // Chiều cao tự động theo nội dung
        )
        layoutParams.width = 170  // Chiều rộng của ảnh, ví dụ 150px
        layoutParams.height = 170 // Chiều cao của ảnh, ví dụ 150px
        imageView.layoutParams = layoutParams

        // Thiết lập nội dung cho TextView (trạng thái thanh toán)
        textView.text = message
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.setPadding(0, 35, 0, 0)

        // Thêm ImageView và TextView vào layout
        layout.addView(imageView)
        layout.addView(textView)

        // Tạo và hiển thị AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setView(layout) // Đặt layout vào AlertDialog
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        // Tạo Dialog
        val dialog = builder.create()

        // Căn giữa Dialog và thiết lập kích thước
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ) // Kích thước nhỏ
        window?.setGravity(Gravity.CENTER) // Căn giữa Dialog

        // Hiển thị Dialog
        dialog.show()
    }

    private fun addHoaDon(
//        id_NguoiDung: String,
//        id_Coupon: String,
//        ngayNhanPhong: String,
//        ngayTraPhong: String,
//        tongKhach: Int,
//        tongPhong: Int,
//        ngayThanhToan: String,
//        phuongThucThanhToan: String,
//        trangThai: Int, tongTien: Double,
//        chiTiet: ArrayList<ChiTietHoaDonModel>
    ) {
        Log.d(
            "BookingFragmentLinh",
            "addHoaDon: Đang thêm hóa đơn, phương thức thanh toán: $phuongThucThanhToan"
        )

        chiTiet.add(ChiTietHoaDonModel("67458eb205f65d34c273f847", 2, 20000.0, false))

        val hoaDon = HoaDonModel(
            "67383ffd4bd6355e37a1d253",
            "674dea63dd8162fb5938cd22",
            "2024-12-06",
            "2024-12-08",
            2,
            2,
            "2024-12-07",
            "zaloPay",
            1,
            200000.0,
            chiTiet
        )

        val hoaDonJson = Gson().toJson(hoaDon)
        Log.d("BookingActivity", "HoaDon JSON: $hoaDonJson")

        // Gọi API thêm hóa đơn
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("BookingFragmentLinh", "addHoaDon: Gửi yêu cầu thêm hóa đơn đến server.")
                val response = hoaDonService.addHoaDon(hoaDon)

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        // Xử lý kết quả thành công (update UI, thông báo)
                        // Ví dụ: Hiển thị thông báo hóa đơn đã được thêm thành công
                        Log.d("BookingFragmentLinh", "addHoaDon: Hóa đơn được tạo thành công.")
                        showPaymentDialog(
                            "Thanh toán thành công",
                            "Hóa đơn đã được tạo thành công!",
                            R.drawable.img_16
                        )
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    // Ví dụ: Hiển thị thông báo lỗi
                    withContext(Dispatchers.Main) {
                        Log.e(
                            "BookingFragmentLinh",
                            "addHoaDon: Lỗi khi tạo hóa đơn, mã lỗi: ${response.code()}"
                        )
                        showPaymentDialog(
                            "Lỗi",
                            "Có lỗi khi tạo hóa đơn. Vui lòng thử lại!",
                            R.drawable.img_18
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Xử lý ngoại lệ nếu có lỗi trong quá trình gửi yêu cầu API
            }
        }
    }

    private fun fetchUserProfile(id: String) {
        lifecycleScope.launch {
            try {
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == id }
                    if (user != null) {
                        updateUI(user)
                    } else {
                        tenKH.text = "Không tìm thấy tên"
                        sdtKH.text = "Không tìm thấy số điện thoại"
                    }
                } else {
                    Log.e("BookingActivity", "API thất bại: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("BookingActivity", "Lỗi khi gọi API: ${e.message}")
            }
        }
    }

    private fun updateUI(user: NguoiDungModel) {
        lifecycleScope.launch(Dispatchers.Main) {
            tenKH.text = user.tenNguoiDung ?: "Không có tên"
            sdtKH.text = user.soDienThoai ?: "Không có số điện thoại"
        }
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(amount) + " đ"
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}

