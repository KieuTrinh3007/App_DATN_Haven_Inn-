package com.example.app_datn_haven_inn.ui.room.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.BaseFragment
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.FavoriteRequest
import com.example.app_datn_haven_inn.database.model.YeuThichModel
import com.example.app_datn_haven_inn.databinding.FragmentPhongNghiBinding
import com.example.app_datn_haven_inn.ui.room.adapter.PhongNghiAdapter
import com.example.app_datn_haven_inn.viewModel.LoaiPhongViewModel
import com.example.app_datn_haven_inn.viewModel.YeuThichViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale


class PhongNghiFragment : BaseFragment<FragmentPhongNghiBinding>() {

    private lateinit var sharedPreferences: SharedPreferences
    private var adapter: PhongNghiAdapter? = null
    private lateinit var loaiPhongViewModel: LoaiPhongViewModel
    private lateinit var yeuThichViewModel: YeuThichViewModel

    override fun inflateViewBinding(): FragmentPhongNghiBinding {
        return FragmentPhongNghiBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("idNguoiDung", "")
        yeuThichViewModel.getFavoritesByUserId(idUser.toString())
        yeuThichViewModel.yeuThichList1.observe(viewLifecycleOwner) { updatedList ->
            if (updatedList != null) {
                adapter?.listPhong?.forEach { phong ->
                    phong.isFavorite = updatedList.any { it.id == phong.id }
                }
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun initView() {
        super.initView()

        viewBinding.rcvDanhSachPhong.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhongNghiAdapter(emptyList(), requireContext())
        viewBinding.rcvDanhSachPhong.adapter = adapter

        loaiPhongViewModel = ViewModelProvider(requireActivity())[LoaiPhongViewModel::class.java]
        yeuThichViewModel = ViewModelProvider(requireActivity())[YeuThichViewModel::class.java]

        loaiPhongViewModel.getListloaiPhong()

        loaiPhongViewModel.loaiPhongList.observe(viewLifecycleOwner) { list ->
            Log.d("PhongNghiFragment", "List size: ${list?.size}")
            if (list != null) {
                adapter?.let {
                    it.listPhong = list
                    it.notifyDataSetChanged()
                }
            }
        }

        loaiPhongViewModel.loaiPhongList.observe(viewLifecycleOwner, Observer { list ->
            adapter?.notifyDataSetChanged()
        })

        viewBinding.txtTatCaPhong.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_64A89C
            )
        )
        viewBinding.txtTatCaPhong.setOnClickListener {
            loaiPhongViewModel.getListloaiPhong()

            viewBinding.txtTatCaPhong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_64A89C
                )
            )
            viewBinding.txt1Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            viewBinding.txt2Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )


        }

        viewBinding.txt1Giuong.setOnClickListener {
            val loaiPhongList = loaiPhongViewModel.loaiPhongList.value
            val filteredList = loaiPhongList?.filter { loaiPhong ->
                loaiPhong.giuong.contains("Một")
            }
            adapter?.let {
                if (filteredList != null) {
                    it.updateList(filteredList)
                }
            }
            viewBinding.txt1Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_64A89C
                )
            )
            viewBinding.txtTatCaPhong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            viewBinding.txt2Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )

        }

        viewBinding.txt2Giuong.setOnClickListener {
            val loaiPhongList = loaiPhongViewModel.loaiPhongList.value
            val filteredList = loaiPhongList?.filter { loaiPhong ->
                loaiPhong.giuong.contains("Hai")
            }
            adapter?.let {
                if (filteredList != null) {
                    it.updateList(filteredList)
                }
            }
            viewBinding.txt2Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_64A89C
                )
            )
            viewBinding.txtTatCaPhong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            viewBinding.txt1Giuong.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )

        }


        adapter?.setonFavotiteSelected { phong ->
            val sharedPreferences =
                requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val idNguoiDung = sharedPreferences?.getString("idNguoiDung", null)
            Log.d("PhongNghiFragment", "idNguoiDung: $idNguoiDung")
            if (idNguoiDung == null) {
                Toast.makeText(
                    requireActivity(),
                    "Không tìm thấy thông tin người dùng",
                    Toast.LENGTH_SHORT
                ).show()
                return@setonFavotiteSelected
            }

            if (phong.isFavorite) {
                val yeuThich = FavoriteRequest(idNguoiDung, phong.id)
                yeuThichViewModel.addyeuThich(yeuThich)

                yeuThichViewModel.isyeuThichAdded.observe(viewLifecycleOwner) { success ->
                    Log.d("PhongNghiFragment", "Thêm yêu thích thành công: $success")
                    if (success) {
                        yeuThichViewModel.getFavoritesByUserId(idNguoiDung)
                    } else {
                        phong.isFavorite = false
                        adapter?.notifyItemChanged(adapter?.listPhong?.indexOf(phong) ?: 0)
                    }
                }
            } else {

                yeuThichViewModel.deleteyeuThich(phong.id, idNguoiDung)


                yeuThichViewModel.isyeuThichDeleted.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        yeuThichViewModel.getFavoritesByUserId(idNguoiDung)
                    } else {
                        phong.isFavorite = true
                        adapter?.notifyItemChanged(adapter?.listPhong?.indexOf(phong) ?: 0)
                    }
                }
            }
        }


    }
}