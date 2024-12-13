package com.example.app_datn_haven_inn.ui.profile

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
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
import com.example.app_datn_haven_inn.ui.cccd.CaptureFrontActivity
import com.example.app_datn_haven_inn.ui.cccd.CccdGuide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.dialog.DialogSignIn
import com.example.app_datn_haven_inn.ui.coupon.CouponActivity
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

    private val danhGiaService = CreateService.createService<DanhGiaService>()

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

        // test danh gia
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0, nên cần +1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24h format
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val currentTime = "$day/$month/$year $hour:$minute:$second"
        println("Thời gian hiện tại: $currentTime")

        feedback.setOnClickListener {
            openDanhGiaDialog(idNguoiDung!!, "6744740f2fc05a4fc789b59b", currentTime)
        }
        //////////////////////////////

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
            val intent = Intent(requireContext(), SignIn::class.java)
            startActivity(intent)
            activity?.finish()
        }

        dialog.show()
    }

    private fun openDanhGiaDialog(idNguoiDung: String, idLoaiPhong: String, ngayDanhGia: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_danh_gia)
        dialog.setCancelable(true)

        // Áp dụng nền trong suốt cho dialog
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ratingBar = dialog.findViewById<RatingBar>(R.id.rating_bar)
        val etComment = dialog.findViewById<EditText>(R.id.et_comment)
        val btnSubmit = dialog.findViewById<TextView>(R.id.btn_submit)
        val ivFeedbackIcon = dialog.findViewById<ImageView>(R.id.iv_feedback_icon)

        // Áp dụng hiệu ứng chuyển động mượt cho icon khi thay đổi rating
        val feedbackAnimation = ObjectAnimator.ofFloat(ivFeedbackIcon, "scaleX", 0.5f, 1f).apply {
            duration = 300
        }
        val feedbackAnimationY = ObjectAnimator.ofFloat(ivFeedbackIcon, "scaleY", 0.5f, 1f).apply {
            duration = 300
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Cập nhật biểu tượng cảm xúc dựa trên số sao đã chọn
            val feedbackIcon = when {
                rating >= 4 -> R.drawable.happiness // 4 sao hoặc hơn -> Cảm xúc vui
                rating >= 2 -> R.drawable.face // Từ 2 đến 4 sao -> Cảm xúc trung bình
                else -> R.drawable.vanh1 // Dưới 2 sao -> Cảm xúc buồn
            }
            ivFeedbackIcon.setImageResource(feedbackIcon)

            // Áp dụng hiệu ứng chuyển động
            feedbackAnimation.start()
            feedbackAnimationY.start()
        }

        btnSubmit.setOnClickListener {
            val soDiem = ratingBar.rating * 2 // Quy đổi ra điểm (1 sao = 2 điểm)
            val binhLuan = etComment.text.toString()

            if (soDiem.toDouble() == 0.0 || binhLuan.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng nhập đầy đủ thông tin!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val danhGia = DanhGiaModel(
                id = UUID.randomUUID().toString(),
                id_NguoiDung = idNguoiDung,
                id_LoaiPhong = idLoaiPhong,
                soDiem = soDiem.toDouble(),
                binhLuan = binhLuan,
                ngayDanhGia = ngayDanhGia
            )

            lifecycleScope.launch {
                try {
                    val response =
                        withContext(Dispatchers.IO) { danhGiaService.addDanhGia(danhGia) }
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Đánh giá thành công!", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    } else {
                        showError("Gửi đánh giá thất bại!")
                    }
                } catch (e: Exception) {
                    showError("Có lỗi xảy ra: ${e.message}")
                }
            }
        }

        // Tạo hiệu ứng mở dialog mượt mà
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
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

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
