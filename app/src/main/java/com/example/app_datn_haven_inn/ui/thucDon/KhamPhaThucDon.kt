package com.example.app_datn_haven_inn.ui.thucDon

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.databinding.ActivityKhamPhaThucDonBinding
import com.github.chrisbanes.photoview.PhotoView

class KhamPhaThucDon : AppCompatActivity() {

    private lateinit var binding: ActivityKhamPhaThucDonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKhamPhaThucDonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data passed via Intent
        val amThuc = intent.getParcelableExtra<AmThucModel>("amThuc")

        amThuc?.let {
            binding.titleKhamPhaThucDon.text = it.tenNhaHang
            binding.viTriKhamPhaThucDon.text = "Vị trí: ${it.viTri}"
            binding.gioKhamPhaThucDon.text = "Giờ hoạt động: ${it.gioMoCua} - ${it.gioDongCua}"
            binding.hotlineKhamPhaThucDon.text = "Hotline: ${it.hotline}"

            Glide.with(this)
                .load(it.hinhAnh)
                .placeholder(R.drawable.img_20)
                .error(R.drawable.iv_dot_off)
                .into(binding.imgKhamPhaThucDon)

            val menuAdapter = MenuAdapter(it.menu, ::showImageDialog)
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewMenu.adapter = menuAdapter
        }

        // Handle back button click
        binding.root.findViewById<ImageView>(R.id.ic_back_td).setOnClickListener {
            finish()
        }
    }

    // Function to show full-screen image in a dialog
    private fun showImageDialog(imageUrl: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_fullscreen_image)

        val photoView: PhotoView = dialog.findViewById(R.id.fullscreenImageView)
        val closeButton: ImageView = dialog.findViewById(R.id.closeButton)

        // Sử dụng Glide để tải hình ảnh vào PhotoView
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.img_20)
            .error(R.drawable.bg_radius_5_yellow)
            .into(photoView)

        // Đảm bảo dialog chiếm toàn bộ màn hình
        val params = dialog.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = params

        // Xử lý đóng dialog khi nhấn nút đóng
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}
