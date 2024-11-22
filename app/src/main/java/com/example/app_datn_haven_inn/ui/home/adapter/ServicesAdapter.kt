package com.example.app_datn_haven_inn.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.DichVuModel

class ServicesAdapter : RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    private val servicesList = mutableListOf<DichVuModel>()

    fun setServices(services: List<DichVuModel>) {
        servicesList.clear()
        servicesList.addAll(services)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_services, parent, false)
        return ServicesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val service = servicesList[position]
        holder.bind(service)
    }

    override fun getItemCount(): Int = servicesList.size

    class ServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTenDichVu: TextView = itemView.findViewById(R.id.tvTenDichVu)
        private val tvMoTa: TextView = itemView.findViewById(R.id.tvMoTa)

        fun bind(service: DichVuModel) {
            tvTenDichVu.text = service.tenDichVu
            tvMoTa.text = formatMoTa(service.moTa)
        }

        private fun formatMoTa(moTa: String): String {
            // Chia chuỗi bằng ký tự '\n' và thêm dấu chấm tròn đầu mỗi dòng
            return moTa.split("\n").joinToString("\n") { "•  $it" }
        }
    }
}
