package com.example.app_datn_haven_inn.ui.home.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.repository.LoaiPhongRepository
import com.example.app_datn_haven_inn.database.service.AmThucService
import com.example.app_datn_haven_inn.database.service.LoaiPhongService
import com.example.app_datn_haven_inn.ui.home.adapter.AmThucAdapter
import com.example.app_datn_haven_inn.ui.home.adpter.RoomTopAdapter


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class OverviewFragment : Fragment() {

    private lateinit var recyclerViewFood: RecyclerView
    private lateinit var recyclerViewRoomTop: RecyclerView
    private lateinit var amThucAdapter: AmThucAdapter
    private lateinit var roomTopAdapter: RoomTopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        // Khởi tạo RecyclerView cho món ăn
        recyclerViewFood = view.findViewById(R.id.recyclerViewFood)
        recyclerViewFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Khởi tạo RecyclerView cho danh sách phòng
        recyclerViewRoomTop = view.findViewById(R.id.recyclerViewRoomTop)
        recyclerViewRoomTop.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Load dữ liệu
        loadAmThucData()
        loadRoomTopData()

        return view
    }

    private fun loadAmThucData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<AmThucModel>> = CreateService.createService<AmThucService>().getListAmThuc()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val amThucList = response.body() ?: emptyList()
                    amThucAdapter = AmThucAdapter(requireContext(), amThucList)
                    recyclerViewFood.adapter = amThucAdapter
                } else {
                    // Xử lý lỗi nếu cần
                }
            }
        }
    }

    private fun loadRoomTopData() {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = LoaiPhongRepository(CreateService.createService<LoaiPhongService>())
            val roomList = repository.getListLoaiPhong()

            withContext(Dispatchers.Main) {
                if (!roomList.isNullOrEmpty()) {
                    roomTopAdapter = RoomTopAdapter(requireContext(), roomList)
                    recyclerViewRoomTop.adapter = roomTopAdapter
                } else {
                    // Xử lý lỗi nếu không có dữ liệu
                }
            }
        }
    }
}
