package com.example.app_datn_haven_inn.ui.room

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.databinding.FragmentRoomBinding
import com.example.app_datn_haven_inn.ui.review.adapter.ReviewAdapter
import com.example.app_datn_haven_inn.ui.review.model.Review
import com.example.app_datn_haven_inn.ui.room.adapter.PhotoAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiNoiBatAdapter
import com.example.app_datn_haven_inn.ui.room.adapter.TienNghiPhongAdapter
import com.example.app_datn_haven_inn.ui.room.model.Photo
import com.example.app_datn_haven_inn.ui.room.model.TienNghiPhong

import java.util.Timer
import java.util.TimerTask

import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent

class RoomFragment : Fragment() {

    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoAdapter: PhotoAdapter
    private var timer: Timer? = null
    private val mListphoto: MutableList<Photo> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentRoomBinding.inflate(inflater, container, false)

        // Load slideshow
        loadSlide()

        // Set up button click events
        binding.btnBack.setOnClickListener {
            moveToPreviousSlide()
        }

        binding.btnNext.setOnClickListener {
            moveToNextSlide()
        }

        loadTienNghiNoiBat()
        loadTienNghiPhong()
        loadReview()

        return binding.root
    }

    private fun loadTienNghiNoiBat(){
        val rvTienNghi = binding.rvTiennghi
        val layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
        rvTienNghi.layoutManager = layoutManager
        val adapter = TienNghiNoiBatAdapter(listOf("Wi-Fi", "TV", "Điều hòa", "Máy sấy", "Tủ lạnh", "Bàn làm việc", "Máy giặt"))
        rvTienNghi.adapter = adapter
    }

    private fun loadSlide() {
        // Prepare the list of photos
        getListphoto(mListphoto)
        // Set up the adapter and bind it to the ViewPager
        photoAdapter = PhotoAdapter(requireContext(), mListphoto)
        binding.viewpager.adapter = photoAdapter

        // Set up CircleIndicator with ViewPager
        binding.circleIndicator.setViewPager(binding.viewpager)
        photoAdapter.registerDataSetObserver(binding.circleIndicator.dataSetObserver)

        // Start the auto slideshow
//        autoSlideshow(mListphoto)
    }

    private fun loadTienNghiPhong(){
        val rvTienNghi = binding.rvTiennghiphong
        rvTienNghi.layoutManager = LinearLayoutManager(context) // Sử dụng LinearLayoutManager dọc
        val adapter = TienNghiPhongAdapter(listOf(
            TienNghiPhong("Phòng tắm", "Áo choàng tắm\n" +
                    "Máy sấy tóc\n" +
                    "Phòng tắm riêng\n" +
                    "Buồng tắm vòi sen\n" +
                    "Dép đi trong nhà\n" +
                    "Khăn tắm", R.drawable.ic_bath),
            TienNghiPhong("Phòng ngủ", "Máy điều hòa nhiệt độ\n" +
                    "Bộ trải giường\n" +
                    "Màn/rèm cản sáng\n" +
                    "Giường gấp/giường phụ\n" +
                    "Sofa giường", R.drawable.ic_bed2),
            TienNghiPhong("Ăn uống", "Nước đóng chai miễn phí\n" +
                    "Minibar\n" +
                    "Dịch vụ phòng (giới hạn thời gian)", R.drawable.ic_food2),
            TienNghiPhong("Giải trí", "TV LCD 43 inch\n" +
                    "Truyền hình cáp\n" +
                    "Truyền hình cao cấp", R.drawable.ic_ytb),
            TienNghiPhong("Internet", "Wifi miễn phí\n" +
                    "Truy cập Internet có dây miễn phí", R.drawable.ic_wifi2),
            TienNghiPhong("Không gian ngoài trời", "Ban công", R.drawable.ic_beach2),
            TienNghiPhong("Khác", "Dịch vụ dọn phòng hàng ngày\n" +
                    "Bàn\n" +
                    "Lò sưởi\n" +
                    "Thay bộ trải giường theo yêu cầu\n" +
                    "Thay khăn theo yêu cầu\n" +
                    "Bàn ủi/dụng cụ ủi quần áo (theo yêu cầu)\n" +
                    "Điện thoại\n" +
                    "Phòng cách âm\n" +
                    "Tủ quần áo", R.drawable.ic_v2),
        ))
        rvTienNghi.adapter = adapter
    }

    private fun loadReview() {
        val allReviews = listOf(
            Review(R.drawable.avat2, "Lê Đăng Sang", 9, "Khách sạn có dịch vụ rất tốt, nhân viên nhiệt tình lịch sự, phòng ốc trang nhã sạch sẽ", "12/10/2024"),
            Review(R.drawable.avatar1, "Vũ Thị Vân Anh", 10, "Good place to stay", "1/10/2024"),
            Review(R.drawable.ic_person, "Nguyễn Văn A", 8, "Dịch vụ tốt!", "01/11/2023"),
            Review(R.drawable.person13x13, "Trần Thị B", 9, "Rất hài lòng.", "15/11/2023")
        )

        // Lấy 2 bình luận đầu tiên để hiển thị ban đầu
        val initialReviews = allReviews.take(2)

        val adapter = ReviewAdapter(initialReviews.toMutableList())
        binding.rvReview.layoutManager = LinearLayoutManager(context)
        binding.rvReview.adapter = adapter

        // Kiểm tra nếu có hơn 2 bình luận để hiển thị TextView "Xem tất cả bình luận"
        if (allReviews.size > 2) {
            binding.txtSeeAllReviews.visibility = View.VISIBLE

            // Xử lý sự kiện bấm vào "Xem tất cả bình luận"
            binding.txtSeeAllReviews.setOnClickListener {
                adapter.updateReviews(allReviews)  // Cập nhật RecyclerView với tất cả bình luận
                binding.txtSeeAllReviews.visibility = View.GONE  // Ẩn TextView sau khi bấm
            }
        } else {
            binding.txtSeeAllReviews.visibility = View.GONE
        }
    }


    private fun getListphoto(list: MutableList<Photo>) {
        list.clear()
        list.add(Photo(R.drawable.room_1))
        list.add(Photo(R.drawable.room_2))
        list.add(Photo(R.drawable.room_1))
        list.add(Photo(R.drawable.room_3))
        list.add(Photo(R.drawable.room_4))
    }

    private fun autoSlideshow(mListphoto: List<Photo>) {
        if (mListphoto.isEmpty()) {
            return
        }

        // Initialize timer
        timer?.cancel()
        timer = Timer()

        timer?.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    moveToNextSlide()
                }
            }
        }, 3000, 3000)
    }

    private fun moveToNextSlide() {
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        if (currentItem < totalItem) {
            binding.viewpager.currentItem = currentItem + 1
        } else {
            binding.viewpager.currentItem = 0
        }
    }

    private fun moveToPreviousSlide() {
        val currentItem = binding.viewpager.currentItem
        val totalItem = mListphoto.size - 1
        if (currentItem > 0) {
            binding.viewpager.currentItem = currentItem - 1
        } else {
            binding.viewpager.currentItem = totalItem
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        timer = null
        _binding = null
    }
}
