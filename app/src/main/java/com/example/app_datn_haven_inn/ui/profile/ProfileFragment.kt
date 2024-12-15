package com.example.app_datn_haven_inn.ui.profile

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.DanhGiaModel
import com.example.app_datn_haven_inn.database.model.NguoiDungModel
import com.example.app_datn_haven_inn.database.service.CccdService
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.ui.auth.RePassword
import com.example.app_datn_haven_inn.ui.auth.SignIn
import com.example.app_datn_haven_inn.ui.cccd.CccdGuide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.ui.coupon.CouponActivity
import com.example.app_datn_haven_inn.ui.dieuKhoan.GioiThieuActivity
import com.example.app_datn_haven_inn.ui.dieuKhoan.dieuKhoan
import com.example.app_datn_haven_inn.ui.history.LichSuDatPhongActivity
import com.example.app_datn_haven_inn.ui.myRoom.MyRoomActivity
import com.example.app_datn_haven_inn.utils.SharePrefUtils
import java.util.Calendar

class ProfileFragment : Fragment() {
    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvemail: TextView
    private lateinit var bt_edit_profile: ImageView
    private lateinit var xmcccd: TextView
    private lateinit var doiMK: TextView
    private lateinit var feedback: TextView
    private lateinit var bt_signout: ImageView
    private lateinit var discountCode: TextView
    private lateinit var Policy: TextView
    private lateinit var about: TextView

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
        feedback = view.findViewById(R.id.feedback)
        discountCode = view.findViewById(R.id.discountCode)
        Policy = view.findViewById(R.id.Policy)
        about = view.findViewById(R.id.about)

        val tvMyRoom = view.findViewById<TextView>(R.id.myRoom)
        val tvLsDatPhong = view.findViewById<TextView>(R.id.transactionHistory)

        val idNguoiDung = arguments?.getString("idNguoiDung")
        idNguoiDung?.let { fetchUserProfile(it) }

        discountCode.setOnClickListener {
            val intent = Intent(requireContext(), CouponActivity::class.java)
            startActivity(intent)
        }

        tvLsDatPhong.setOnClickListener {
            // Xử lý chuyển màn hình sang MyRoomActivity
            val intent = Intent(requireContext(), LichSuDatPhongActivity::class.java)
            intent.putExtra("idNguoiDung", idNguoiDung) // Truyền ID người dùng
            startActivity(intent)
        }
        
        Policy.setOnClickListener {
            val intent = Intent(requireContext(), dieuKhoan::class.java)
            startActivity(intent)
        }
        
        about.setOnClickListener {
            val intent = Intent(requireContext(), GioiThieuActivity::class.java)
            startActivity(intent)
        }

        tvMyRoom.setOnClickListener {
            // Xử lý chuyển màn hình sang MyRoomActivity
            val intent = Intent(requireContext(), MyRoomActivity::class.java)
            intent.putExtra("idNguoiDung", idNguoiDung) // Truyền ID người dùng
            startActivity(intent)
        }
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
            // Gọi hàm getUserById trong một coroutine
            lifecycleScope.launch {
                val user = getUserById(idNguoiDung) // Hàm lấy thông tin người dùng từ bộ nhớ tạm hoặc server
                if (user?.xacMinh == true) {
                    showDialogAccountVerified() // Hiển thị thông báo tài khoản đã xác minh
                } else {
                    val intent = Intent(requireContext(), CccdGuide::class.java)
                    intent.putExtra("idNguoiDung", idNguoiDung)
                    startActivity(intent) // Chuyển hướng người dùng đến hướng dẫn CCCD
                }
            }
        }

        bt_signout.setOnClickListener {
            showLogoutDialog()
        }

        feedback.setOnClickListener {

        }

        return view
    }

    // Phương thức tải lại thông tin người dùng
    private fun fetchUserProfile(id: String) {
        lifecycleScope.launch {
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

    private fun showLogoutDialog() {
        // Inflate layout từ XML để tạo dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()

        val btnCancel = dialogView.findViewById<TextView>(R.id.dialog_cancel_button)
        val btnLogout = dialogView.findViewById<TextView>(R.id.dialog_logout_button)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            val intent = Intent(requireContext(), SignIn::class.java)
            startActivity(intent)
            activity?.finish()
        }

        dialog.show()
    }

    private suspend fun getUserById(idNguoiDung: String?): NguoiDungModel? {
        val response = nguoiDungService.getListNguoiDung()
        return response.body()?.find { it.id == idNguoiDung }
    }

    // Hiển thị dialog khi tài khoản đã được xác thực
    private fun showDialogAccountVerified() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Tài khoản của bạn đã được xác thực!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
