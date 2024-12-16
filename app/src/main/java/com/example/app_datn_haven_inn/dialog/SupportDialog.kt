package com.example.app_datn_haven_inn.ui.support

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.HoTroModel
import com.example.app_datn_haven_inn.databinding.DialogSupportBinding
import com.example.app_datn_haven_inn.viewModel.HoTroViewModel

class SupportDialog(
    context: Context,
    private val viewModel: HoTroViewModel,
    private val userId: String
) : Dialog(context, R.style.CustomDialogTheme) {
    private val binding: DialogSupportBinding = DialogSupportBinding.inflate(layoutInflater)
    private val observer = Observer<Boolean> { success ->
        if (success) {
            Toast.makeText(context, "Gửi hỗ trợ thành công!", Toast.LENGTH_SHORT).show()
            dismiss()
        } else {
            Toast.makeText(context, "Gửi hỗ trợ thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT)
                .show()
        }
    }
    init {
        setContentView(binding.root) // Đặt giao diện dialog
        window!!.attributes.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        setupListeners() // Thiết lập lắng nghe sự kiện
        observeViewModel() // Quan sát trạng thái từ ViewModel
        show() // Hiển thị dialog ngay khi được khởi tạo
    }
    
    private fun setupListeners() {
        // Xử lý sự kiện khi nhấn nút "Gửi"
        binding.btnSend.setOnClickListener {
            val vanDe = binding.etVanDe.text.toString().trim()
            
            // Kiểm tra dữ liệu nhập
            if (vanDe.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập vấn đề cần hỗ trợ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Tạo yêu cầu hỗ trợ
            val supportRequest = HoTroModel(
                id = "",
                id_NguoiDung = userId,
                vanDe = vanDe,
                trangThai = 0
            )
            
            // Gửi yêu cầu hỗ trợ qua ViewModel
            viewModel.addhoTro(supportRequest)
        }
        
        // Xử lý sự kiện khi nhấn nút "Đóng"
        binding.btnClose.setOnClickListener {
            dismiss() // Đóng dialog
        }
    }
    
    private fun observeViewModel() {
        viewModel.ishoTroAdded.observeForever(observer)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Hủy observer khi dialog bị đóng để tránh rò rỉ bộ nhớ
        viewModel.ishoTroAdded.removeObserver(observer)
    }
}
