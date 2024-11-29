package com.example.app_datn_haven_inn.ui.thucDon

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.food.adapter.AmThucAdapter
import com.example.app_datn_haven_inn.viewModel.AmThucViewModel

class ThucDonFragment : AppCompatActivity() {

    private lateinit var amThucViewModel: AmThucViewModel
    private lateinit var adapter: AmThucAdapter

    // Ánh xạ các thành phần UI
    private lateinit var recyclerViewThucDon: RecyclerView
    private lateinit var progressBarThucDon: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_thucdon)

        // Ánh xạ UI
        recyclerViewThucDon = findViewById(R.id.viewThucDon)
        progressBarThucDon = findViewById(R.id.progressBarThucdon)

        // Thiết lập ViewModel
        amThucViewModel = ViewModelProvider(this).get(AmThucViewModel::class.java)
        amThucViewModel.getListamThuc()

        // Lắng nghe danh sách món ăn
        amThucViewModel.amThucList.observe(this, Observer { amThucList ->
            if (amThucList != null && amThucList.isNotEmpty()) {
                adapter = AmThucAdapter(amThucList) { amThuc ->
                    Toast.makeText(this, "Clicked: ${amThuc.tenNhaHang}", Toast.LENGTH_SHORT).show()
                }

                recyclerViewThucDon.layoutManager = LinearLayoutManager(this)
                recyclerViewThucDon.adapter = adapter
                progressBarThucDon.visibility = View.GONE // Ẩn ProgressBar khi tải xong dữ liệu
            } else {
                Toast.makeText(this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show()
                progressBarThucDon.visibility = View.GONE
            }
        })

        // Hiển thị thông báo lỗi
        amThucViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                progressBarThucDon.visibility = View.GONE
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
