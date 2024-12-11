package com.example.app_datn_haven_inn.ui.support

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.app_datn_haven_inn.database.model.HoTroModel
import com.example.app_datn_haven_inn.databinding.DialogSupportBinding
import com.example.app_datn_haven_inn.viewModel.HoTroViewModel


class SupportDialog(context: Context, private val viewModel: HoTroViewModel) : Dialog(context) {
    private lateinit var binding: DialogSupportBinding

    fun showDialog() {
        binding = DialogSupportBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val supportContent = binding.etSupport.text.toString()
            viewModel.addhoTro(HoTroModel(
                supportContent,
                id_NguoiDung = TODO(),
                vanDe = "",
                trangThai = 1
            ))
            
            
        }
    }
}