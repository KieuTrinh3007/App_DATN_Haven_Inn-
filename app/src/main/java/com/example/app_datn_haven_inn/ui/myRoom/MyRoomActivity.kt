package com.example.app_datn_haven_inn.ui.myRoom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.ActivityMyRoomBinding
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.ui.support.SupportDialog
import com.example.app_datn_haven_inn.utils.Constans
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.example.app_datn_haven_inn.viewModel.HoTroViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRoomBinding
    private lateinit var adapter: MyRoomAdapter
    private lateinit var hotroViewModel: HoTroViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMyRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerViewMyrRoom.layoutManager = LinearLayoutManager(this)

        // Set up Back Button
        binding.icBackMr.setOnClickListener {
            onBackPressed() // Handles the back navigation
        }
        
        hotroViewModel = ViewModelProvider(this).get(HoTroViewModel::class.java)
        
        val userId = SharedPrefsHelper.getIdNguoiDung(this)
        
        // Xử lý khi bấm FAB
        binding.fabSupport.setOnClickListener {
            if (userId != null) {
                SupportDialog(this, hotroViewModel, userId) // Tạo và hiển thị dialog
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để gửi hỗ trợ!", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch Room Data
        fetchRooms()
    }

//    private fun fetchRooms() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constans.DOMAIN)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(NguoiDungService::class.java)
//        val userId = SharedPrefsHelper.getIdNguoiDung(this)
//
//        if (userId.isNullOrEmpty()) {
//            showMessage("Không tìm thấy ID người dùng!")
//            return
//        }
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val response = service.myRoom(userId)
//                if (response.isSuccessful) {
//                    val phongList = response.body()
//
//                    if (phongList.isNullOrEmpty()) {
//                        withContext(Dispatchers.Main) {
//                            showMessage("Không có phòng nào trong khoảng thời gian hiện tại")
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            // Set the adapter with the room list
//                            adapter = MyRoomAdapter(phongList)
//                            binding.recyclerViewMyrRoom.adapter = adapter
//                        }
//                    }
//                } else {
//                    val errorResponse = response.errorBody()?.string()
//                    Log.e("MyRoomActivity", "API Error: $errorResponse")
//                    withContext(Dispatchers.Main) {
//                        showMessage("Lỗi khi tải danh sách phòng: $errorResponse")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("MyRoomActivity", "Exception: ${e.message}", e)
//                withContext(Dispatchers.Main) {
//                    showMessage("Có lỗi xảy ra khi tải danh sách phòng. Vui lòng thử lại!")
//                }
//            }
//        }
//    }
private fun fetchRooms() {
    val retrofit = Retrofit.Builder()
        .baseUrl(Constans.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(NguoiDungService::class.java)
    val userId = SharedPrefsHelper.getIdNguoiDung(this)

    if (userId.isNullOrEmpty()) {
        showMessage("Không tìm thấy ID người dùng!")
        return
    }

    lifecycleScope.launch(Dispatchers.IO) {
        try {
            val response = service.myRoom(userId)
            if (response.isSuccessful) {
                val phongList = response.body()

                if (phongList.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        showMessage("Không có phòng nào trong khoảng thời gian hiện tại")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Thiết lập adapter với danh sách phòng và callback khi nhấn vào phòng
                        adapter = MyRoomAdapter(phongList) { selectedRoom ->
                            // Chuyển sang màn chi tiết
                            val intent = Intent(this@MyRoomActivity, MyRoomDetailActivity::class.java)
                            intent.putExtra("room_data", selectedRoom)
                            startActivity(intent)
                        }
                        binding.recyclerViewMyrRoom.adapter = adapter
                    }
                }
            } else {
                val errorResponse = response.errorBody()?.string()
                Log.e("MyRoomActivity", "API Error: $errorResponse")
                withContext(Dispatchers.Main) {
                    showMessage("Lỗi khi tải danh sách phòng: $errorResponse")
                }
            }
        } catch (e: Exception) {
            Log.e("MyRoomActivity", "Exception: ${e.message}", e)
            withContext(Dispatchers.Main) {
                showMessage("Có lỗi xảy ra khi tải danh sách phòng. Vui lòng thử lại!")
            }
        }
    }
}


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
