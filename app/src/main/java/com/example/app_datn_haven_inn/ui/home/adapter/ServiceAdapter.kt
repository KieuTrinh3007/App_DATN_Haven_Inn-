package com.example.app_datn_haven_inn.ui.home.Faragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.TienNghiModel
import com.example.app_datn_haven_inn.databinding.ItemServiceBinding

class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private val services = mutableListOf<TienNghiModel>()

    fun setServices(list: List<TienNghiModel>) {
        services.clear()
        services.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size

    class ServiceViewHolder(private val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: TienNghiModel) {
            binding.textServiceName.text = service.tenTienNghi
            Glide.with(binding.root.context)
                .load(service.image) // Load image from URL
                .placeholder(R.drawable.img_20) // Optional: Placeholder while loading
                .error(R.drawable.img_20) // Optional: Error image if URL fails
                .into(binding.imageService)
        }
    }
}
