package com.example.app_datn_haven_inn.ui.home.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.database.service.DichVuService
import com.example.app_datn_haven_inn.database.service.TienNghiService
import com.example.app_datn_haven_inn.databinding.FragmentServiceBinding
import com.example.app_datn_haven_inn.ui.home.Faragment.ServiceAdapter
import com.example.app_datn_haven_inn.ui.home.adapter.ServicesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ServiceAdapter
    private lateinit var adapterServices: ServicesAdapter
    private val danhGiaService = CreateService.createService<DanhGiaService>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập adapter và layout cho RecyclerView đầu tiên
        adapter = ServiceAdapter()
        binding.recyclerViewService.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewService.adapter = adapter

        // Thiết lập adapter và layout cho RecyclerView thứ hai
        adapterServices = ServicesAdapter()
        binding.recyclerViewServices.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewServices.adapter = adapterServices

        // Gọi API để lấy dữ liệu
        fetchServices()
        fetchServices2()

//        openDanhGiaDialog("64b8c5f4d6e8d3221c3a7ba4", "64b8c5f4d6e8d3221c3a7bc2")
    }

    private fun fetchServices() {
        lifecycleScope.launch {
            try {
                val service = CreateService.createService<TienNghiService>()
                val response = withContext(Dispatchers.IO) { service.getListTienNghi() }

                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.setServices(it)
                    }
                } else {
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }

    private fun fetchServices2() {
        lifecycleScope.launch {
            try {
                val service = CreateService.createService<DichVuService>()
                val response = withContext(Dispatchers.IO) { service.getListDichVu() }

                if (response.isSuccessful) {
                    response.body()?.let {
                        adapterServices.setServices(it)
                    }
                } else {
                    handleError(response.code(), response.message())
                }
            } catch (e: Exception) {
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }

//    private fun openDanhGiaDialog(idNguoiDung: String, idLoaiPhong: String) {
//        val dialog = Dialog(requireContext())
//        dialog.setContentView(R.layout.dialog_danh_gia)
//        dialog.setCancelable(true)
//
//        val ratingBar = dialog.findViewById<RatingBar>(R.id.rating_bar)
//        val etComment = dialog.findViewById<EditText>(R.id.et_comment)
//        val btnSubmit = dialog.findViewById<TextView>(R.id.btn_submit)
//        val ivFeedbackIcon = dialog.findViewById<ImageView>(R.id.iv_feedback_icon)
//
//        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
//            // Cập nhật biểu tượng cảm xúc dựa trên số sao đã chọn
//            val feedbackIcon = when {
//                rating >= 4 -> R.drawable.happiness // 4 sao hoặc hơn -> Cảm xúc vui
//                rating >= 2 -> R.drawable.face // Từ 2 đến 4 sao -> Cảm xúc trung bình
//                else -> R.drawable.vanh1 // Dưới 2 sao -> Cảm xúc buồn
//            }
//            ivFeedbackIcon.setImageResource(feedbackIcon) // Cập nhật icon cảm xúc
//        }
//
//        btnSubmit.setOnClickListener {
//            val soDiem = ratingBar.rating * 2 // Quy đổi ra điểm (1 sao = 2 điểm)
//            val binhLuan = etComment.text.toString()
//
//            if (soDiem.toDouble() == 0.0 || binhLuan.isBlank()) {
//                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val danhGia = DanhGiaModel(
//                id = UUID.randomUUID().toString(),
//                id_NguoiDung = idNguoiDung,
//                id_LoaiPhong = idLoaiPhong,
//                soDiem = soDiem.toDouble(),
//                binhLuan = binhLuan
//            )
//
//            lifecycleScope.launch {
//                try {
//                    val response = withContext(Dispatchers.IO) { danhGiaService.addDanhGia(danhGia) }
//                    if (response.isSuccessful) {
//                        Toast.makeText(requireContext(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    } else {
//                        showError("Gửi đánh giá thất bại!")
//                    }
//                } catch (e: Exception) {
//                    showError("Có lỗi xảy ra: ${e.message}")
//                }
//            }
//        }
//
//        val layoutParams = dialog.window?.attributes
//        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT  // Toàn bộ chiều rộng màn hình
//        layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT  // Chiều cao tự động vừa với nội dung
//        dialog.window?.attributes = layoutParams
//
//        dialog.show()
//    }

    private fun handleError(code: Int, message: String) {
        Toast.makeText(requireContext(), "Lỗi [$code]: $message", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
