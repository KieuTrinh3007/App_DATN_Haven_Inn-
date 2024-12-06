package com.example.app_datn_haven_inn.ui.booking

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.Api.CreateOrder
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.ChiTietHoaDonModel
import com.example.app_datn_haven_inn.database.model.HoaDonModel
import com.example.app_datn_haven_inn.database.service.CouponService
import com.example.app_datn_haven_inn.database.service.HoaDonService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.database.service.PhongService
import com.example.app_datn_haven_inn.ui.booking.fragment.PaymentNotification
import com.example.app_datn_haven_inn.ui.coupon.CouponActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class BookingFragment : AppCompatActivity() {

    private lateinit var txtGiaChuaGiam: TextView
    private lateinit var txtGiaDaGiam: TextView
    private lateinit var rdo_zalo: RadioButton
    private lateinit var rdoMomo: RadioButton
    private lateinit var ttZaloPay: LinearLayout
    private lateinit var ttQuaMoMo: LinearLayout
    private lateinit var edtCoupon: EditText
    private lateinit var btnBooking: TextView
    private var isThanhToan = false
    private lateinit var hoaDonService: HoaDonService
    private lateinit var phongService: PhongService
    private lateinit var couponService: CouponService

    var id_NguoiDung: String = ""
    var id_Coupon: String = ""
    var ngayNhanPhong: String = ""
    var ngayTraPhong: String = ""
    var soLuongKhach: Int = 0
    var soLuongPhong: Int = 0
    var ngayThanhToan: String = ""
    var phuongThucThanhToan: String = ""
    var trangThai: Int = 1
    var chiTiet: ArrayList<ChiTietHoaDonModel> = ArrayList()
    var tongTien: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_booking)

        // Ánh xạ View
        txtGiaChuaGiam = findViewById(R.id.txt_giaChuaGiam)
        txtGiaDaGiam = findViewById(R.id.txt_giaDaGiam)
        rdo_zalo = findViewById(R.id.rdo_zalo)
        rdoMomo = findViewById(R.id.rdo_momo)
        ttZaloPay = findViewById(R.id.ttZaloPay)
        ttQuaMoMo = findViewById(R.id.ttQuaMoMo)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnBooking = findViewById(R.id.btnBooking)

        hoaDonService = CreateService.createService()

        txtGiaChuaGiam.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

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

        addHoaDon(
            id_NguoiDung, id_Coupon, ngayNhanPhong, ngayTraPhong, soLuongKhach,
            soLuongPhong, ngayThanhToan, phuongThucThanhToan, trangThai, tongTien, chiTiet
        )

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val intent = intent

        val totalString = String.format("%.0f", 1000000.0)

        btnBooking.setOnClickListener {
            val orderApi = CreateOrder()
            try {
                val data = orderApi.createOrder(totalString)
                val code = data.getString("return_code")
                if (code == "1") {
                    val token = data.getString("zp_trans_token")
                    ZaloPaySDK.getInstance().payOrder(
                        this@BookingFragment,
                        token,
                        "demozpdk://app",
                        object : PayOrderListener {
                            override fun onPaymentSucceeded(
                                p1: String?,
                                p2: String?,
                                p3: String?
                            ) {
                                // Hiển thị Dialog thông báo thanh toán thành công
                                showPaymentDialog(
                                    "Thanh toán thành công",
                                    "Bạn đã thanh toán thành công!",
                                    R.drawable.img_16
                                )
                            }

                            override fun onPaymentCanceled(p1: String?, p2: String?) {
                                // Hiển thị Dialog thông báo thanh toán bị hủy
                                showPaymentDialog(
                                    "Thanh toán bị hủy",
                                    "Bạn đã hủy thanh toán!",
                                    R.drawable.img_18
                                )
                            }

                            override fun onPaymentError(
                                error: ZaloPayError?,
                                p1: String?,
                                p2: String?
                            ) {
                                // Hiển thị Dialog thông báo lỗi thanh toán
                                showPaymentDialog(
                                    "Lỗi thanh toán",
                                    "Đã xảy ra lỗi khi thanh toán. Vui lòng thử lại!",
                                    R.drawable.img_18
                                )
                            }
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        edtCoupon.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val intent = Intent(this, CouponActivity::class.java)
                startActivityForResult(intent, 100)
                return@setOnTouchListener true
            }
            false
        }

        tongTien = 1000000.0

        couponService = CreateService.createService<CouponService>()

        addHoaDon(
            id_NguoiDung, id_Coupon, ngayNhanPhong, ngayTraPhong, soLuongKhach,
            soLuongPhong, ngayThanhToan, phuongThucThanhToan, trangThai, tongTien, chiTiet
        )
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
        id_NguoiDung: String,
        id_Coupon: String,
        ngayNhanPhong: String,
        ngayTraPhong: String,
        soLuongKhach: Int,
        soLuongPhong: Int,
        ngayThanhToan: String,
        phuongThucThanhToan: String,
        trangThai: Int, tongTien: Double,
        chiTiet: ArrayList<ChiTietHoaDonModel>
    ) {
        Log.d(
            "BookingFragmentLinh",
            "addHoaDon: Đang thêm hóa đơn, phương thức thanh toán: $phuongThucThanhToan"
        )

        val hoaDon = HoaDonModel(
            id = null,
            id_NguoiDung,
            id_Coupon,
            ngayNhanPhong,
            ngayTraPhong,
            soLuongKhach,
            soLuongPhong,
            ngayThanhToan,
            phuongThucThanhToan,
            trangThai,
            tongTien,
            chiTiet
        )

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}
