package com.example.app_datn_haven_inn.ui.booking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.Api.CreateOrder
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.ui.booking.fragment.PaymentNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingActivity : AppCompatActivity() {
    private lateinit var txtGiaChuaGiam: TextView
    private lateinit var txtGiaDaGiam: TextView
    private lateinit var rdoTructiep: RadioButton
    private lateinit var rdoMomo: RadioButton
    private lateinit var ttTrucTiep: LinearLayout
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
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService()
    }
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_booking)

        // truyen du lieu
        val selectedRooms = intent.getParcelableArrayListExtra<PhongModel>("selectedRooms")
        val gia = intent.getStringExtra("totalPrice")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val llPhongContainer = findViewById<LinearLayout>(R.id.llPhongContainer)


        // anh xa
        txtGiaChuaGiam = findViewById(R.id.txt_giaChuaGiam)
        txtGiaDaGiam = findViewById(R.id.txt_giaDaGiam)
        rdoTructiep = findViewById(R.id.rdo_tructiep)
        rdoMomo = findViewById(R.id.rdo_momo)
        ttTrucTiep = findViewById(R.id.ttTrucTiep)
        ttQuaMoMo = findViewById(R.id.ttQuaMoMo)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnBooking = findViewById(R.id.btnBooking)
        icBack = findViewById(R.id.ic_back)
        ngayNhan = findViewById(R.id.txt_ngayNhanPhong)
        ngayTra = findViewById(R.id.txt_ngayTraPhong)
        soDem = findViewById(R.id.txt_soDem)
        tenKH = findViewById(R.id.txt_tenKhachHang)
        sdtKH = findViewById(R.id.txt_SDT_khachHang)

        // gan du lieu
        ngayNhan.text = startDate
        ngayTra.text = endDate

        val idNguoiDung = intent.getStringExtra("idNguoiDung")
            ?: getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("idNguoiDung", null)

        idNguoiDung?.let { fetchUserProfile(it) } ?: run {
            Log.e("BookingActivity", "Không tìm thấy ID người dùng")
            tenKH.text = "Không tìm thấy tên"
            sdtKH.text = "Không tìm thấy số điện thoại"
        }

        if (selectedRooms != null) {
            val guestCountsMap =
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
                val numberOfNights = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
                soDem.text = "$numberOfNights đêm lưu trú"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BookingFragment", "Lỗi khi tính số đêm: ${e.message}")
        }


        resizeImage()
        icBack.setOnClickListener {
            finish()

        }
        // Gạch ngang cho TextView
        txtGiaChuaGiam.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        ttTrucTiep.setOnClickListener {
            rdoTructiep.isChecked = true
            rdoMomo.isChecked = false
            isThanhToan = true
        }

        ttQuaMoMo.setOnClickListener {
            rdoMomo.isChecked = true
            rdoTructiep.isChecked = false
            isThanhToan = false
        }

        // Lắng nghe sự kiện click vào ic_drop (drawableEnd)
        edtCoupon.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showCouponMenu(v)
                return@setOnTouchListener true
            }
            false
        }

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val totalString = String.format("%.0f", 1000000.0)

        btnBooking.setOnClickListener {
            val orderApi = CreateOrder()
            try {
                val data = orderApi.createOrder(totalString)
                val code = data.getString("return_code")
                if (code == "1") {
                    val token = data.getString("zp_trans_token")
                    ZaloPaySDK.getInstance().payOrder(
                        this@BookingActivity,
                        token,
                        "demozpdk://app",
                        object : PayOrderListener {
                            override fun onPaymentSucceeded(p1: String?, p2: String?, p3: String?) {
                                val intent =
                                    Intent(this@BookingActivity, PaymentNotification::class.java)
                                intent.putExtra("result", "Thanh toán thành công")
                                startActivity(intent)
                            }

                            override fun onPaymentCanceled(p1: String?, p2: String?) {
                                val intent =
                                    Intent(this@BookingActivity, PaymentNotification::class.java)
                                intent.putExtra("result", "Hủy thanh toán")
                                startActivity(intent)
                            }

                            override fun onPaymentError(
                                error: ZaloPayError?,
                                p1: String?,
                                p2: String?
                            ) {
                                val intent =
                                    Intent(this@BookingActivity, PaymentNotification::class.java)
                                intent.putExtra("result", "Lỗi thanh toán")
                                startActivity(intent)
                            }
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
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

    private fun showCouponMenu(view: View) {
        // Tạo PopupMenu để hiển thị danh sách mã giảm giá
        val popupMenu = PopupMenu(this, view)

        // Thêm các mục vào PopupMenu (Danh sách mã giảm giá)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, "Mã giảm giá 1 - 10%")
        menu.add(0, 2, 0, "Mã giảm giá 2 - 20%")
        menu.add(0, 3, 0, "Mã giảm giá 3 - 30%")
        menu.add(0, 4, 0, "Mã giảm giá 4 - 40%")

        // Xử lý sự kiện khi người dùng chọn một mã giảm giá
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                1 -> edtCoupon.setText("Mã giảm giá 1 - 10%")
                2 -> edtCoupon.setText("Mã giảm giá 2 - 20%")
                3 -> edtCoupon.setText("Mã giảm giá 3 - 30%")
                4 -> edtCoupon.setText("Mã giảm giá 4 - 40%")
            }
            true
        }

        // Hiển thị PopupMenu
        popupMenu.show()
    }

    private fun resizeImage() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_coupon_2)
        val drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_drop)

        // Thay đổi kích thước Drawable
        drawable?.setBounds(0, 0, 50, 50) // width và height (pixel)
        drawable2?.setBounds(0, 0, 50, 50) // width và height (pixel)

        edtCoupon.setCompoundDrawablesRelative(drawable, null, drawable2, null)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}
