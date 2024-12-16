package com.example.app_datn_haven_inn.ui.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.database.CreateService
import com.example.app_datn_haven_inn.database.model.AmThucModel
import com.example.app_datn_haven_inn.database.model.DanhGiaNguoiDungModel
import com.example.app_datn_haven_inn.database.repository.LoaiPhongRepository
import com.example.app_datn_haven_inn.database.service.AmThucService
import com.example.app_datn_haven_inn.database.service.DanhGiaService
import com.example.app_datn_haven_inn.database.service.LoaiPhongService
import com.example.app_datn_haven_inn.ui.home.adapter.AmThucAdapter
import com.example.app_datn_haven_inn.ui.home.adpter.RoomTopAdapter
import com.example.app_datn_haven_inn.ui.room.RoomDetailActivity
import com.example.app_datn_haven_inn.ui.thucDon.ThucDonFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Locale

class OverviewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var recyclerViewFood: RecyclerView
    private lateinit var recyclerViewRoomTop: RecyclerView
    private lateinit var xemloaiphong: TextView
    private lateinit var xemmonan: TextView
    private lateinit var xemtiennghi: TextView
    private lateinit var amThucAdapter: AmThucAdapter
    private lateinit var roomTopAdapter: RoomTopAdapter
    private lateinit var mMap: GoogleMap
    private lateinit var progressBar: ProgressBar

    // Vị trí mặc định để hiển thị trên bản đồ
    private val location = "113 Hàng Buồm, Phố Cổ, Hà Nội, 10000"
    private val latLng = LatLng(21.033333, 105.85) // Tọa độ địa chỉ (Hà Nội)

    override fun onResume() {
        super.onResume()
        loadRoomTopData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        // Khởi tạo TextView
        xemloaiphong = view.findViewById(R.id.xemloaiphong)
        xemmonan = view.findViewById(R.id.xemmonan)
        xemtiennghi = view.findViewById(R.id.xemtiennghi)
        progressBar = view.findViewById(R.id.progressBarOver)

        xemloaiphong.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager2)
            viewPager.currentItem = 2 // Chỉ mục của PhongNghiFragment trong adapter
        }

        xemmonan.setOnClickListener {
            val intent1 = Intent(requireContext(), ThucDonFragment::class.java)
            startActivity(intent1)
        }

        xemtiennghi.setOnClickListener {
            val intent = Intent(requireContext(), ServiceFragment::class.java)
            startActivity(intent)
        }

        // Khởi tạo RecyclerView cho món ăn
        recyclerViewFood = view.findViewById(R.id.recyclerViewFood)
        recyclerViewFood.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Khởi tạo RecyclerView cho danh sách phòng
        recyclerViewRoomTop = view.findViewById(R.id.recyclerViewRoomTop)
        recyclerViewRoomTop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Load dữ liệu món ăn và phòng
        loadAmThucData()
        loadRoomTopData()

        // Khởi tạo và hiển thị Google Map
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Xử lý sự kiện khi nhấn vào địa chỉ
        val xemtrongbando =
            view.findViewById<LinearLayout>(R.id.xemtrongbando) // ID của TextView "Xem trong bản đồ"
        xemtrongbando.setOnClickListener {
            // Kiểm tra xem thiết bị có ứng dụng Google Maps không
            if (isGoogleMapsAvailable()) {
                openGoogleMaps()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Google Maps không có sẵn trên thiết bị này",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }

    // Kiểm tra xem Google Maps có sẵn trên thiết bị không
    private fun isGoogleMapsAvailable(): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$location"))
        intent.setPackage("com.google.android.apps.maps")
        return intent.resolveActivity(requireActivity().packageManager) != null
    }

    // Mở Google Maps với địa chỉ cụ thể
    private fun openGoogleMaps() {
        val geoUri = Uri.parse("geo:0,0?q=$location") // Địa chỉ đã mã hóa
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        mapIntent.setPackage("com.google.android.apps.maps") // Cấu hình Intent mở Google Maps

        startActivity(mapIntent)
    }

    // Hàm sẽ được gọi khi bản đồ đã sẵn sàng
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap ?: return

        // Gọi phương thức riêng để thiết lập bản đồ
        setupMap()
    }

    // Phương thức riêng để thiết lập bản đồ
    private fun setupMap() {
        // Thêm Marker vào bản đồ tại vị trí đã chỉ định
        mMap.addMarker(MarkerOptions().position(latLng).title("Vị trí của bạn"))

        // Di chuyển camera đến vị trí đó và zoom vào
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    // Tải dữ liệu món ăn
    private fun loadAmThucData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<AmThucModel>> =
                CreateService.createService<AmThucService>().getListAmThuc()
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

    // Tải dữ liệu phòng
    private fun loadRoomTopData() {
        progressBar.visibility = View.VISIBLE
        recyclerViewRoomTop.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            val repository = LoaiPhongRepository(CreateService.createService<LoaiPhongService>())
            val roomList = repository.getListLoaiPhong()

            val danhGiaMap = mutableMapOf<String, Pair<Double, Int>>()

            roomList?.forEach { room ->
                val response: Response<List<DanhGiaNguoiDungModel>> =
                    CreateService.createService<DanhGiaService>()
                        .getListDanhGiaByIdLoaiPhong(room.id)

                if (response.isSuccessful) {
                    val danhGiaList = response.body() ?: emptyList()
                    val soDiem = danhGiaList.sumOf { it.soDiem }
                    val soLuongDanhGia = danhGiaList.size
                    // Tính điểm trung bình
                    val diemTrungBinh = if (soLuongDanhGia > 0) {
                        // Dùng String.format để làm tròn về 1 chữ số sau dấu thập phân
                        String.format(Locale.US, "%.1f", soDiem / soLuongDanhGia).toDouble()
                    } else {
                        0.0
                    }
                    danhGiaMap[room.id] = Pair(diemTrungBinh, soLuongDanhGia)
                }
            }

            withContext(Dispatchers.Main) {
                if (!roomList.isNullOrEmpty()) {
                    roomTopAdapter =
                        RoomTopAdapter(requireContext(), roomList, danhGiaMap) { room ->
                            // Khởi động Activity mới với idLoaiPhong
                            val intent = Intent(requireContext(), RoomDetailActivity::class.java)
                            intent.putExtra("id_LoaiPhong", room.id)
                            intent.putExtra("tenLoaiPhong", room.tenLoaiPhong)
                            intent.putExtra("giuong", room.giuong)
                            intent.putExtra("soLuongKhach", room.soLuongKhach.toString())
                            intent.putExtra("dienTich", room.dienTich.toString())
                            intent.putExtra("hinhAnh", room.hinhAnh.toTypedArray())
                            intent.putExtra("moTa", room.moTa)
                            intent.putExtra("giaTien", room.giaTien.toInt())

                            Log.d(
                                "modelRoomTop", "loadRoomTopData: " + room.id + room.tenLoaiPhong +
                                        room.giuong + room.dienTich + room.hinhAnh + room.moTa
                            )

                            startActivity(intent)
                        }
                    recyclerViewRoomTop.adapter = roomTopAdapter
                    roomTopAdapter.notifyDataSetChanged()
                }
                progressBar.visibility = View.GONE
                recyclerViewRoomTop.visibility = View.VISIBLE
            }

        }
    }
}
