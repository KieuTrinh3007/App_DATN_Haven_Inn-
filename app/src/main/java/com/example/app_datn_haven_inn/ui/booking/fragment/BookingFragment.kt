package com.example.app_datn_haven_inn.ui.booking

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.Api.CreateOrder
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.ui.booking.fragment.PaymentNotification
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class BookingFragment : AppCompatActivity() {

    private lateinit var txtGiaChuaGiam: TextView
    private lateinit var txtGiaDaGiam: TextView
    private lateinit var rdoTructiep: RadioButton
    private lateinit var rdoMomo: RadioButton
    private lateinit var ttTrucTiep: LinearLayout
    private lateinit var ttQuaMoMo: LinearLayout
    private lateinit var edtCoupon: EditText
    private lateinit var btnBooking: TextView
    private var isThanhToan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_booking)

        val phong = intent.getParcelableExtra<PhongModel>("selectedRooms")
        val gia = intent.getStringExtra("totalPrice")

        // Ánh xạ View
        txtGiaChuaGiam = findViewById(R.id.txt_giaChuaGiam)
        txtGiaDaGiam = findViewById(R.id.txt_giaDaGiam)
        rdoTructiep = findViewById(R.id.rdo_tructiep)
        rdoMomo = findViewById(R.id.rdo_momo)
        ttTrucTiep = findViewById(R.id.ttTrucTiep)
        ttQuaMoMo = findViewById(R.id.ttQuaMoMo)
        edtCoupon = findViewById(R.id.edtCoupon)
        btnBooking = findViewById(R.id.btnBooking)

        // Gạch ngang cho TextView
        txtGiaChuaGiam.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        // Set click listeners for LinearLayouts
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

        val intent = intent

        val totalString = String.format("%.0f", 1000000.0)

//        val tongtt = String.format("%.0f", totalString);

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
                            override fun onPaymentSucceeded(p1: String?, p2: String?, p3: String?) {
                                val intent =
                                    Intent(this@BookingFragment, PaymentNotification::class.java)
                                intent.putExtra("result", "Thanh toán thành công")
                                startActivity(intent)
                            }

                            override fun onPaymentCanceled(p1: String?, p2: String?) {
                                val intent =
                                    Intent(this@BookingFragment, PaymentNotification::class.java)
                                intent.putExtra("result", "Hủy thanh toán")
                                startActivity(intent)
                            }

                            override fun onPaymentError(
                                error: ZaloPayError?,
                                p1: String?,
                                p2: String?
                            ) {
                                val intent =
                                    Intent(this@BookingFragment, PaymentNotification::class.java)
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
