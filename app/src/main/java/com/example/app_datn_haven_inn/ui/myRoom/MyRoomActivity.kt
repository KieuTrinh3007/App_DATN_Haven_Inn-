package com.example.app_datn_haven_inn.ui.myRoom

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.model.LoaiPhongModel
import com.example.app_datn_haven_inn.database.model.PhongModel
import com.example.app_datn_haven_inn.database.repository.ApiResponse
import com.example.app_datn_haven_inn.database.service.NguoiDungService
import com.example.app_datn_haven_inn.utils.Constans
import com.example.app_datn_haven_inn.utils.SharedPrefsHelper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyRoomActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room)

        recyclerView = findViewById(R.id.recyclerViewMyrRoom)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchRooms()
    }

    private fun fetchRooms() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constans.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NguoiDungService::class.java)

        val userId = SharedPrefsHelper.getIdNguoiDung(this)

        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.myRoom(userId)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null) {
                        // Kiểm tra xem dữ liệu có tồn tại không
                        if (apiResponse.data.isNullOrEmpty()) {
                            runOnUiThread {
                                Toast.makeText(this@MyRoomActivity, "Không có phòng nào trong khoảng thời gian hiện tại", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Nếu có dữ liệu, hiển thị danh sách phòng
                            val phongList = apiResponse.data

                            runOnUiThread {
                                // Gán lại dữ liệu vào Adapter
                                adapter = MyRoomAdapter(phongList)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()  // Đảm bảo Adapter biết có sự thay đổi dữ liệu
                            }
                        }
                    } else {
                        // Nếu response body là null, hiển thị thông báo lỗi
                        runOnUiThread {
                            Toast.makeText(this@MyRoomActivity, "Lỗi không xác định", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Nếu API trả về lỗi (không thành công), hiển thị thông báo lỗi
                    val errorResponse = response.errorBody()?.string()
                    runOnUiThread {
                        Toast.makeText(this@MyRoomActivity, "Lỗi khi tải danh sách phòng: $errorResponse", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Nếu không có ID người dùng, hiển thị thông báo lỗi
            Toast.makeText(this, "Không tìm thấy ID người dùng!", Toast.LENGTH_SHORT).show()
        }
    }

}
