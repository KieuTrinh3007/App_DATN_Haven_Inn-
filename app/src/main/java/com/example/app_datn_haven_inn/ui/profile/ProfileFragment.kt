package com.example.app_datn_haven_inn.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.app_datn_haven_inn.ui.auth.RePassword
import com.example.app_datn_haven_inn.ui.auth.SignIn
import com.example.app_datn_haven_inn.ui.cccd.CaptureFrontActivity
import com.example.app_datn_haven_inn.ui.cccd.CccdGuide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvemail: TextView
    private lateinit var bt_edit_profile: ImageView
    private lateinit var xmcccd: TextView
    private lateinit var doiMK: TextView
    private lateinit var bt_signout: ImageView
    private val nguoiDungService: NguoiDungService by lazy {
        CreateService.createService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        ivAvatar = view.findViewById(R.id.profileImage)
        tvemail = view.findViewById(R.id.tvemailprofile)
        tvName = view.findViewById(R.id.name)
        bt_edit_profile = view.findViewById(R.id.btn_edit_profile)
        xmcccd = view.findViewById(R.id.xmcccd)
        doiMK = view.findViewById(R.id.changePassword)
        bt_signout = view.findViewById(R.id.signOut)

        val idNguoiDung = arguments?.getString("idNguoiDung")
        idNguoiDung?.let { fetchUserProfile(it) }

        bt_edit_profile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfile::class.java)
            intent.putExtra("idNguoiDung", idNguoiDung)
            startActivityForResult(intent, 100)  // Sử dụng startActivityForResult để nhận kết quả
        }

        doiMK.setOnClickListener {
            val intent1 = Intent(requireContext(), RePassword::class.java)
            intent1.putExtra("idNguoiDung", idNguoiDung)
            startActivityForResult(intent1, 100)
        }

        Log.e("ProfileFragment", "idNguoiDung is null or empty" + idNguoiDung)

        xmcccd.setOnClickListener {
            val intent = Intent(requireContext(), CccdGuide::class.java)
            intent.putExtra("idNguoiDung", idNguoiDung)
            startActivity(intent)
        }

        bt_signout.setOnClickListener {
            val intent = Intent(requireContext(), SignIn::class.java)
            startActivity(intent)
        }


        return view
    }

    // Phương thức tải lại thông tin người dùng
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

    // Cập nhật giao diện với thông tin người dùng mới
    private suspend fun updateUI(user: NguoiDungModel) {
        withContext(Dispatchers.Main) {
            tvName.text = user.tenNguoiDung
            tvemail.text = user.email
            Glide.with(this@ProfileFragment)
                .load(user.hinhAnh)
                .into(ivAvatar)
        }
    }

    // Xử lý kết quả từ EditProfile khi người dùng đã lưu thay đổi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            // Kiểm tra nếu có cập nhật mới
            val updated = data?.getBooleanExtra("updated", false) ?: false
            if (updated) {
                val idNguoiDung = arguments?.getString("idNguoiDung")
                idNguoiDung?.let { fetchUserProfile(it) }
            }
        }
    }
}
