package com.example.app_datn_haven_inn.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_datn_haven_inn.R;
import com.example.app_datn_haven_inn.database.model.AmThucModel;

import java.util.List;

public class AmThucAdapter extends RecyclerView.Adapter<AmThucAdapter.AmThucViewHolder> {

    private final Context context;
    private final List<AmThucModel> amThucList;

    public AmThucAdapter(Context context, List<AmThucModel> amThucList) {
        this.context = context;
        this.amThucList = amThucList;
    }

    @NonNull
    @Override
    public AmThucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new AmThucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmThucViewHolder holder, int position) {
        // Get current item in the list
        AmThucModel amThuc = amThucList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(amThuc.getHinhAnh())  // Image URL from AmThucModel
                .placeholder(R.drawable.img_3) // Default image while loading
                .error(R.drawable.vanh) // Image to show if loading fails
                .into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return amThucList.size();
    }

    // ViewHolder class to hold references to views
    public static class AmThucViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName;

        public AmThucViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            imgFood = itemView.findViewById(R.id.imageViewFood);
        }
    }
}
