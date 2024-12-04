package com.example.app_datn_haven_inn.ui.TermsAndPolicies

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_datn_haven_inn.R

class TermsAndPoliciesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_policies)

        // Ánh xạ LinearLayout
        val layoutPolicies: LinearLayout = findViewById(R.id.layoutPolicies)

        // Tạo TextView đầu tiên cho tiêu đề
        val tvTitle = TextView(this)
        tvTitle.text = "Điều Khoản và Chính Sách"
        tvTitle.textSize = 24f
        tvTitle.setTypeface(tvTitle.typeface, Typeface.BOLD)
        tvTitle.setTextColor(resources.getColor(android.R.color.black, theme))
        tvTitle.setPadding(0, 0, 0, 16)
        layoutPolicies.addView(tvTitle)

        // Tạo TextView cho chính sách 1
        val tvPolicy1 = TextView(this)
        tvPolicy1.text = "1. Quy định về đặt phòng:\n- Khách hàng phải cung cấp thông tin chính xác.\n- Việc hủy phòng phải tuân thủ chính sách hủy của khách sạn."
        tvPolicy1.textSize = 16f
        tvPolicy1.setLineSpacing(8f, 1.2f)
        tvPolicy1.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy1.typeface = Typeface.SANS_SERIF
        tvPolicy1.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy1)

        // Tạo TextView cho chính sách 2
        val tvPolicy2 = TextView(this)
        tvPolicy2.text = "2. Chính sách bảo mật:\n- Chúng tôi cam kết bảo mật thông tin cá nhân của khách hàng.\n- Thông tin của bạn sẽ không được chia sẻ nếu không có sự đồng ý."
        tvPolicy2.textSize = 16f
        tvPolicy2.setLineSpacing(8f, 1.2f)
        tvPolicy2.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy2.typeface = Typeface.SANS_SERIF
        tvPolicy2.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy2)

        // Tạo TextView cho chính sách 3
        val tvPolicy3 = TextView(this)
        tvPolicy3.text = "3. Chính sách hủy phòng:\n- Hủy trước 24 giờ không mất phí.\n- Hủy sau 24 giờ sẽ chịu phí hủy theo quy định."
        tvPolicy3.textSize = 16f
        tvPolicy3.setLineSpacing(8f, 1.2f)
        tvPolicy3.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy3.typeface = Typeface.SANS_SERIF
        tvPolicy3.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy3)

        // Tạo TextView cho chính sách 4
        val tvPolicy4 = TextView(this)
        tvPolicy4.text = "4. Thanh toán:\n- Chúng tôi chấp nhận thẻ tín dụng, chuyển khoản ngân hàng và thanh toán trực tiếp.\n- Hóa đơn sẽ được gửi qua email đã đăng ký."
        tvPolicy4.textSize = 16f
        tvPolicy4.setLineSpacing(8f, 1.2f)
        tvPolicy4.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy4.typeface = Typeface.SANS_SERIF
        tvPolicy4.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy4)

        // Tạo TextView cho chính sách 5
        val tvPolicy5 = TextView(this)
        tvPolicy5.text = "5. Quy định về lưu trú:\n- Không mang theo vật nuôi nếu khách sạn không cho phép.\n- Khách hàng chịu trách nhiệm về hành vi của mình trong thời gian lưu trú."
        tvPolicy5.textSize = 16f
        tvPolicy5.setLineSpacing(8f, 1.2f)
        tvPolicy5.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy5.typeface = Typeface.SANS_SERIF
        tvPolicy5.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy5)

        // Tạo TextView cho chính sách 6
        val tvPolicy6 = TextView(this)
        tvPolicy6.text = "6. Thời gian nhận và trả phòng:\n- Nhận phòng từ 14:00.\n- Trả phòng trước 12:00 trưa hôm sau."
        tvPolicy6.textSize = 16f
        tvPolicy6.setLineSpacing(8f, 1.2f)
        tvPolicy6.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy6.typeface = Typeface.SANS_SERIF
        tvPolicy6.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy6)

        // Tạo TextView cho chính sách 7
        val tvPolicy7 = TextView(this)
        tvPolicy7.text = "7. Liên hệ hỗ trợ:\n- Mọi thắc mắc vui lòng gọi số hotline: 1900-xxxx hoặc email: support@haveninn.com."
        tvPolicy7.textSize = 16f
        tvPolicy7.setLineSpacing(8f, 1.2f)
        tvPolicy7.setTextColor(resources.getColor(android.R.color.black, theme))
        tvPolicy7.typeface = Typeface.SANS_SERIF
        tvPolicy7.setPadding(0, 16, 0, 0)
        layoutPolicies.addView(tvPolicy7)

        // Tạo nút quay lại
        val backButton = Button(this)
        backButton.text = "Quay lại"
        backButton.textSize = 16f
        backButton.setTextColor(resources.getColor(android.R.color.white, theme))
        backButton.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, theme))
        backButton.setPadding(16, 16, 16, 16)
        backButton.setOnClickListener {
            finish()
        }
        layoutPolicies.addView(backButton)
    }
}
