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
import com.example.app_datn_haven_inn.ui.auth.Forgot_password
import com.example.app_datn_haven_inn.ui.auth.Indentity_authentication
import com.example.app_datn_haven_inn.ui.auth.SignIn
import com.example.app_datn_haven_inn.ui.auth.SignUp
import com.example.app_datn_haven_inn.ui.booking.fragment.BookingFragment
import com.example.app_datn_haven_inn.ui.main.MainActivity
import com.example.app_datn_haven_inn.ui.room.TuyChinhDatPhongActivity
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
    private lateinit var productViewModel: AmThucViewModel
    private lateinit var productViewModel1: ChiTietHoaDonViewModel
    private lateinit var productViewModel2: CouponViewModel
    private lateinit var productViewModel3: DanhGiaViewModel
    private lateinit var productViewModel4: DichVuViewModel
    private lateinit var productViewModel5: HoaDonViewModel
    private lateinit var productViewModel6: LoaiPhongViewModel
    private lateinit var productViewModel7: NguoiDungViewModel
    private lateinit var productViewModel8: PhongViewModel
    private lateinit var productViewModel9: ThongBaoViewModel
    private lateinit var productViewModel10: TienNghiViewModel
    private lateinit var productViewModel11: TienNghiPhongViewModel
    private lateinit var productViewModel12: YeuThichViewModel
    private lateinit var productViewModel13: HoTroViewModel


    override fun initView() {
        super.initView()

        productViewModel = ViewModelProvider(this)[AmThucViewModel::class.java]
        productViewModel1 = ViewModelProvider(this)[ChiTietHoaDonViewModel::class.java]
        productViewModel2 = ViewModelProvider(this)[CouponViewModel::class.java]
        productViewModel3 = ViewModelProvider(this)[DanhGiaViewModel::class.java]
        productViewModel4 = ViewModelProvider(this)[DichVuViewModel::class.java]
        productViewModel5 = ViewModelProvider(this)[HoaDonViewModel::class.java]
        productViewModel6 = ViewModelProvider(this)[LoaiPhongViewModel::class.java]
        productViewModel7 = ViewModelProvider(this)[NguoiDungViewModel::class.java]
        productViewModel8 = ViewModelProvider(this)[PhongViewModel::class.java]
        productViewModel9 = ViewModelProvider(this)[ThongBaoViewModel::class.java]
        productViewModel10 = ViewModelProvider(this)[TienNghiViewModel::class.java]
        productViewModel11 = ViewModelProvider(this)[TienNghiPhongViewModel::class.java]
        productViewModel12 = ViewModelProvider(this)[YeuThichViewModel::class.java]
        productViewModel13 = ViewModelProvider(this)[HoTroViewModel::class.java]

        productViewModel.getListamThuc() // gọi cái này để call list về trước
        productViewModel1.getListchiTietHoaDon() // gọi cái này để call list về trước
        productViewModel2.getListcoupon()
        productViewModel3.getListdanhGia()
        productViewModel4.getListdichVu()
        productViewModel5.getListhoaDon()
        productViewModel6.getListloaiPhong()
        productViewModel7.getListNguoiDung()
        productViewModel8.getListphong()
        productViewModel9.getListthongBao()
        productViewModel10.getListtienNghi()
        productViewModel11.getListtienNghiPhong()
        productViewModel12.getListyeuThich()
        productViewModel13.getListhoTro()



        productViewModel.amThucList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "amThucList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel1.chiTietHoaDonList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "chiTietHoaDonList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel2.couponList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "couponList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel3.danhGiaList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "danhGiaList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel4.dichVuList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "dichVuList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel5.hoaDonList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "hoaDonList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel6.loaiPhongList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "loaiPhongList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel7.nguoiDungList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "nguoiDungList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel8.phongList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "phongList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel9.thongBaoList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "thongBaoList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel10.tienNghiList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "tienNghiList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel11.tienNghiPhongList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "tienNghiPhongList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel12.yeuThichList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "yeuThichList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

        productViewModel13.hoTroList.observe(this) { productList ->
            Log.d(
                "TAG12345",
                "hoTroList: $productList"
            ) // lấy cái list này  gắn lên recycleview của product là được mà, còn chia nó theo category thì tự xử lý
        }

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