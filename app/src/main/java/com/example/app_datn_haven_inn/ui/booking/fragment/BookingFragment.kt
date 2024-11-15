package com.example.app_datn_haven_inn.ui.booking.fragment

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentBookingBinding

class BookingFragment : Fragment(R.layout.fragment_booking) {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using binding
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Gạch ngang cho TextView
        binding.txtGiaChuaGiam.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        // Set click listeners for LinearLayouts
        binding.ttTrucTiep.setOnClickListener {
            binding.rdoTructiep.isChecked = true
            binding.rdoMomo.isChecked = false
        }

        binding.ttQuaMoMo.setOnClickListener {
            binding.rdoMomo.isChecked = true
            binding.rdoTructiep.isChecked = false

        }
        // Lắng nghe sự kiện click vào ic_drop (drawableEnd)
        binding.edtCoupon.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
//                val drawableEnd = 2
//                if (event.rawX >= (binding.edtCoupon.right - binding.edtCoupon.compoundDrawables[drawableEnd].bounds.width())) {
                    showCouponMenu(v)
                    return@setOnTouchListener true
//                }
            }
            false
        }



    }

    private fun showCouponMenu(view: View) {
        // Tạo PopupMenu để hiển thị danh sách mã giảm giá
        val popupMenu = PopupMenu(requireContext(), view)

        // Thêm các mục vào PopupMenu (Danh sách mã giảm giá)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, "Mã giảm giá 1 - 10%")
        menu.add(0, 2, 0, "Mã giảm giá 2 - 20%")
        menu.add(0, 3, 0, "Mã giảm giá 3 - 30%")
        menu.add(0, 4, 0, "Mã giảm giá 4 - 40%")

        // Xử lý sự kiện khi người dùng chọn một mã giảm giá
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                1 -> binding.edtCoupon.setText("Mã giảm giá 1 - 10%")
                2 -> binding.edtCoupon.setText("Mã giảm giá 2 - 20%")
                3 -> binding.edtCoupon.setText("Mã giảm giá 3 - 30%")
                4 -> binding.edtCoupon.setText("Mã giảm giá 4 - 40%")
            }
            true
        }

        // Hiển thị PopupMenu
        popupMenu.show()
    }
    fun resize_image(){
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_coupon_2)
        val drawable2 = ContextCompat.getDrawable(requireContext(), R.drawable.ic_drop)

        // Thay đổi kích thước Drawable
        drawable?.setBounds(0, 0, 50, 50) // width và height (pixel)
        drawable?.setBounds(0, 0, 50, 50) // width và height (pixel)

        binding.edtCoupon.setCompoundDrawablesRelative(drawable, null, null, drawable2 )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
