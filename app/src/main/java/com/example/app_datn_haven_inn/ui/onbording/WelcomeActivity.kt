package com.example.app_datn_haven_inn.ui.onbording

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.auth.SignIn
import com.example.app_datn_haven_inn.ui.auth.SignUp
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView

    private val slides = listOf(
        SlideItem(R.drawable.onbording1, "Chào Mừng Đến Với Haven Inn!", "Thư giãn trong không gian thanh lịch của Haven Inn, nơi mọi chi tiết đều mang lại cảm giác ấm cúng và riêng tư."),
        SlideItem(R.drawable.onbording2, "Tận Hưởng Những Khoảnh Khắc Trọn Vẹn", "Trải nghiệm lưu trú tinh tế với dịch vụ tận tâm và tiện ích hiện đại, được thiết kế dành riêng cho bạn tại Haven Inn. "),
        SlideItem(R.drawable.onbording3, "Hành Trình Bắt Đầu Tại Haven Inn", "Bắt đầu kỳ nghỉ đẳng cấp, nơi phong cách và sự thoải mái hòa quyện trong từng chi tiết, chỉ có tại Haven Inn.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        viewPager = findViewById(R.id.slideViewPager)
        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotOnbd)

        // Gán adapter trước
        val adapter = SlideAdapter(slides)
        viewPager.adapter = adapter

        // Sau đó gọi setViewPager2
        dotsIndicator.setViewPager2(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val slide = slides[position]
                tvTitle.text = slide.title
                tvDescription.text = slide.description
                btnNext.text = if (position == slides.size - 1) "START" else "NEXT"
            }
        })

        btnSkip.setOnClickListener {
            navigateToSignUp()
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem += 1
            } else {
                navigateToSignUp()
            }
        }
    }


    private fun navigateToSignUp() {
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
        finish()
    }
}
