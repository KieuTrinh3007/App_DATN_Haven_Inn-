package com.example.app_datn_haven_inn.ui.support

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.example.app_datn_haven_inn.database.model.HoTroModel
import com.example.app_datn_haven_inn.databinding.DialogSupportBinding
import com.example.app_datn_haven_inn.viewModel.HoTroViewModel

class SupportDialog(
    context: Context,
    private val viewModel: HoTroViewModel,
    private val userId: String
) : Dialog(context) {
    private val binding: DialogSupportBinding = DialogSupportBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val vanDe = binding.etVanDe.text.toString().trim()


            if (vanDe.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập vấn đề cần hỗ trợ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val supportRequest = HoTroModel(
                id = "",
                id_NguoiDung = userId,
                vanDe = vanDe,
                trangThai = 0
            )


            viewModel.addhoTro(supportRequest)


            Toast.makeText(context, "Gửi hỗ trợ thành công!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    fun showDialog() {
        show()
    }
}
