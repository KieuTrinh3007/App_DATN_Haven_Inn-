package com.example.app_datn_haven_inn.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.app_datn_haven_inn.BaseActivity
import com.example.app_datn_haven_inn.BaseViewModel
import com.example.app_datn_haven_inn.databinding.ActivitySplashBinding
import com.example.app_datn_haven_inn.ui.main.MainActivity
import com.example.app_datn_haven_inn.viewModel.AmThucViewModel
import com.example.app_datn_haven_inn.viewModel.ChiTietHoaDonViewModel
import com.example.app_datn_haven_inn.viewModel.CouponViewModel
import com.example.app_datn_haven_inn.viewModel.DanhGiaViewModel
import com.example.app_datn_haven_inn.viewModel.DichVuViewModel
import com.example.app_datn_haven_inn.viewModel.HoTroViewModel
import com.example.app_datn_haven_inn.viewModel.HoaDonViewModel
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.NguoiDungViewModel
import com.example.app_datn_haven_inn.viewModel.PhongViewModel
import com.example.app_datn_haven_inn.viewModel.ThongBaoViewModel
import com.example.app_datn_haven_inn.viewModel.TienNghiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.TienNghiViewModel
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    private val handler = Handler(Looper.getMainLooper())
    override fun createBinding() = ActivitySplashBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

        Thread(Runnable {
            for (progress in 0..100) {
                handler.post {
                    binding.pbSplash.progress = progress
                    binding.tvPersent.text = "$progress%"
                }
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (progress == 100) {

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
            }
        }).start()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }
}