package com.example.app_datn_haven_inn.ui.thucDon
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.databinding.ActivityKhamPhaThucDonBinding

class KhamPhaThucDon : AppCompatActivity() {

    private lateinit var binding: ActivityKhamPhaThucDonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKhamPhaThucDonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy dữ liệu được truyền qua Intent
        val amThuc = intent.getParcelableExtra<AmThucModel>("amThuc")

        amThuc?.let {
            binding.titleKhamPhaThucDon.text = it.tenNhaHang
            binding.viTriKhamPhaThucDon.text = "Vị trí: ${it.viTri}"
            binding.gioKhamPhaThucDon.text = "Giờ hoạt động: ${it.gioMoCua} - ${it.gioDongCua}"
            binding.hotlineKhamPhaThucDon.text = "Hotline: ${it.hotline}"

            Glide.with(this)
                .load(it.hinhAnh)
                .placeholder(R.drawable.avat2)
                .error(R.drawable.iv_dot_off)
                .into(binding.imgKhamPhaThucDon)

            val menuAdapter = MenuAdapter(it.menu)
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewMenu.adapter = menuAdapter
        }

        // Xử lý nút quay lại
        binding.root.findViewById<ImageView>(R.id.ic_back_td).setOnClickListener {
            finish()
        }
    }
}
