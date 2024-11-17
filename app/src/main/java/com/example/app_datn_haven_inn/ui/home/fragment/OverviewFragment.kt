package com.example.app_datn_haven_inn.ui.home.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.home.adapter.FoodAdapter
import com.example.app_datn_haven_inn.ui.home.model.Food

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        // Khởi tạo RecyclerView và Adapter
        val recyclerViewFood = view.findViewById<RecyclerView>(R.id.recyclerViewFood)
        val foodList = listOf(
            Food("https://th.bing.com/th/id/OIP.vM3rkFirqy92148BzAIBZwHaEK?rs=1&pid=ImgDetMain"),
            Food("https://th.bing.com/th/id/R.aac7cab309ddb377d0110f3cd71312f9?rik=%2b867S%2bQ5QNRyGA&riu=http%3a%2f%2fecolonomics.org%2fwp-content%2fuploads%2f2014%2f09%2fsushi-354628_1280.jpg&ehk=dPsMbeMRWoP6%2f1PLEzAeNJOUNNwUPY8q83%2fJDLUN63c%3d&risl=&pid=ImgRaw&r=0"),
            Food("https://th.bing.com/th/id/R.da0f9d4f79ede796113d198e964631b1?rik=ba4nm8LaaalLow&pid=ImgRaw&r=0")
        )

        val adapter = FoodAdapter(requireContext(), foodList)
        recyclerViewFood.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFood.adapter = adapter

        return view
    }
}
