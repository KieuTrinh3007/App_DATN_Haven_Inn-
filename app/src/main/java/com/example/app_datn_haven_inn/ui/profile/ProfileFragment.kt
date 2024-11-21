package com.example.app_datn_haven_inn.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvsdt: TextView
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        ivAvatar = view.findViewById(R.id.profileImage)
        tvsdt = view.findViewById(R.id.phoneNumber)
        tvName = view.findViewById(R.id.name)

        val idNguoiDung = arguments?.getString("idNguoiDung")
        idNguoiDung?.let { fetchUserProfile(it) }

        return view
    }

    private fun fetchUserProfile(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = nguoiDungService.getListNguoiDung()
                if (response.isSuccessful) {
                    val user = response.body()?.find { it.id == id }
                    user?.let { updateUI(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun updateUI(user: NguoiDungModel) {
        withContext(Dispatchers.Main) {
            tvName.text = user.tenNguoiDung
            tvsdt.text = user.soDienThoai
            Glide.with(this@ProfileFragment)
                .load(user.hinhAnh)
                .into(ivAvatar)
        }
    }
}
